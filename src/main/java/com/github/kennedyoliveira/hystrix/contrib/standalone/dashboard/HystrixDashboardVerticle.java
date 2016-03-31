package com.github.kennedyoliveira.hystrix.contrib.standalone.dashboard;

import io.netty.handler.codec.http.HttpResponseStatus;
import io.vertx.core.AbstractVerticle;
import io.vertx.core.Future;
import io.vertx.core.http.HttpHeaders;
import io.vertx.core.http.HttpServerOptions;
import io.vertx.ext.web.Router;
import io.vertx.ext.web.handler.StaticHandler;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * <p>Verticle for the Dashboard web app.</p>
 * <p>Serves static content and proxy the streams</p>
 *
 * @author Kennedy Oliveira
 */
public class HystrixDashboardVerticle extends AbstractVerticle {

  private final static Logger log = LoggerFactory.getLogger(HystrixDashboardVerticle.class);

  @Override
  public void start(Future<Void> startFuture) throws Exception {
    vertx.sharedData().getCounter("instance-count", counterAsync -> {
      if (counterAsync.failed()) {
        startFuture.fail(counterAsync.cause());
      } else {
        counterAsync.result()
                    .incrementAndGet(counter -> {
                      if (counter.failed()) {
                        startFuture.fail(counter.cause());
                      } else {
                        log.info("Initializing the HystrixDashboardVerticle instance {} - {}", counter.result(), deploymentID());
                        initialize(startFuture);
                      }
                    });
      }
    });
  }

  /**
   * Initialize the Verticle and setup the Http Server
   *
   * @param startFuture The start future to pass the initialization result
   */
  private void initialize(Future<Void> startFuture) {
    final Router hystrixRouter = Router.router(vertx);

    // proxy stream handler
    hystrixRouter.get("/proxy.stream").handler(HystrixDashboardProxyConnectionHandler.create());
    hystrixRouter.route("/*").handler(StaticHandler.create());

    final Router mainRouter = Router.router(vertx);

    // if send a route without the trailing '/' some problems will occur, so i redirect the guy using the trailing '/'
    mainRouter.route("/hystrix-dashboard").handler(context -> {
      if (context.request().path().endsWith("/")) {
        context.next();
      } else {
        context.response().setStatusCode(HttpResponseStatus.MOVED_PERMANENTLY.code()).putHeader(HttpHeaders.LOCATION, "/hystrix-dashboard/").end();
      }
    });
    mainRouter.mountSubRouter("/hystrix-dashboard", hystrixRouter);

    final HttpServerOptions options = new HttpServerOptions().setTcpKeepAlive(true)
                                                             .setIdleTimeout(10000)
                                                             .setPort(Configuration.SERVER_PORT)
                                                             .setHost(Configuration.BIND_ADDRESS)
                                                             .setCompressionSupported(!Configuration.DISABLE_COMPRESSION);

    vertx.createHttpServer(options)
         .requestHandler(mainRouter::accept)
         .listen(result -> {
           if (result.failed()) {
             startFuture.fail(result.cause());
           } else {
             startFuture.complete();
           }
         });
  }
}
