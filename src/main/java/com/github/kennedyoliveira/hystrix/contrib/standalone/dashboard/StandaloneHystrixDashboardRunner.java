package com.github.kennedyoliveira.hystrix.contrib.standalone.dashboard;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Vertx;
import io.vertx.core.logging.SLF4JLogDelegateFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * <p>Main class to start the standalone application</p>
 *
 * @author Kennedy Oliveira
 */
@Slf4j
public class StandaloneHystrixDashboardRunner {

  public static void main(String[] args) {
    // set logging to slf4j
    System.setProperty("vertx.logger-delegate-factory-class-name", SLF4JLogDelegateFactory.class.getName());

    log.info("Initializing...");

    final long start = System.currentTimeMillis();

    log.info("Server configuration [serverPort: {}, bindAddress: {}, Compression Disabled: {}, Total Instances: {}]",
             Configuration.SERVER_PORT,
             Configuration.BIND_ADDRESS,
             Configuration.DISABLE_COMPRESSION,
             Configuration.INSTANCES);

    final Vertx vertx = Vertx.vertx();

    // deploy the verticle
    vertx.deployVerticle(HystrixDashboardVerticle.class.getName(),
                         new DeploymentOptions().setInstances(Configuration.INSTANCES),
                         startFuture -> {
                           if (startFuture.failed()) {
                             log.error("Failed to initialize the Hystrix Dashboard Verticle", startFuture.cause());
                             System.exit(127);
                           }
                           log.info("Verticle [{}] deployed successfully in {} millis!", startFuture.result(), System.currentTimeMillis() - start);
                           log.info("Listening on port: {}", Configuration.SERVER_PORT);
                           log.info("Access the dashboard in your browser: http://{}:{}/hystrix-dashboard/",
                                    Configuration.BIND_ADDRESS.equals("0.0.0.0") ? "localhost" : Configuration.BIND_ADDRESS, // NOPMD
                                    Configuration.SERVER_PORT);
                         });
  }
}
