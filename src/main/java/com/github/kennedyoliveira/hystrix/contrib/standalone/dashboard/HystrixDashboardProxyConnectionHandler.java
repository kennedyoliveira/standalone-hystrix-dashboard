package com.github.kennedyoliveira.hystrix.contrib.standalone.dashboard;

import io.vertx.core.Handler;
import io.vertx.core.MultiMap;
import io.vertx.core.Vertx;
import io.vertx.core.VertxException;
import io.vertx.core.buffer.Buffer;
import io.vertx.core.http.*;
import io.vertx.core.streams.Pump;
import io.vertx.ext.web.RoutingContext;
import lombok.extern.slf4j.Slf4j;

import java.net.URI;
import java.nio.charset.StandardCharsets;

/**
 * This handler will proxy connection to a Hystrix Event Metrics stream using Basic Auth if present, and compressing the response.
 *
 * @author Kennedy Oliveira
 */
@Slf4j
public class HystrixDashboardProxyConnectionHandler implements Handler<RoutingContext> {

  /**
   * Creates a new {@link HystrixDashboardProxyConnectionHandler}, that will handle proxying connection to a Hystrix Event Metrics stream using Basic Auth if present
   *
   * @return The new {@link HystrixDashboardProxyConnectionHandler}
   */
  static HystrixDashboardProxyConnectionHandler create() {
    return new HystrixDashboardProxyConnectionHandler();
  }

  @Override
  public void handle(RoutingContext requestCtx) {
    String proxiedUrl = getProxyUrl(requestCtx);
    if (proxiedUrl == null) return;

    final URI uri;
    uri = createUriFromUrl(proxiedUrl, requestCtx);
    if (uri == null) return;

    proxyRequest(uri, requestCtx);
  }

  /**
   * Extract the url to Proxy.
   *
   * @param requestCtx Context of the current request.
   * @return The url to proxy or null if it wasn't found.
   */
  String getProxyUrl(RoutingContext requestCtx) {
    final HttpServerRequest serverRequest = requestCtx.request();
    final HttpServerResponse serverResponse = requestCtx.response();

    // origin = metrics stream endpoint
    String origin = serverRequest.getParam("origin");
    if (origin == null || origin.isEmpty()) {
      log.warn("Request without origin");
      serverResponse.setStatusCode(500)
                    .end(Buffer.buffer("Required parameter 'origin' missing. Example: 107.20.175.135:7001".getBytes(StandardCharsets.UTF_8)));
      return null;
    }
    origin = origin.trim();

    boolean hasFirstParameter = false;
    StringBuilder url = new StringBuilder();
    // if there is no http, i add
    if (!origin.startsWith("http")) {
      url.append("http://");
    }
    url.append(origin);

    // if contains any query parameter
    if (origin.contains("?")) {
      hasFirstParameter = true;
    }

    // add the request params to the url to proxy, because need to forward Delay and maybe another future param
    MultiMap params = serverRequest.params();
    for (String key : params.names()) {
      if (!"origin".equals(key) && !"authorization".equals(key)) {
        String value = params.get(key);

        if (hasFirstParameter) {
          url.append("&");
        } else {
          url.append("?");
          hasFirstParameter = true;
        }
        url.append(key).append("=").append(value);
      }
    }

    return url.toString();
  }

  /**
   * Tries to transform the {@code proxiedUrl} in a URI, if fails, return a response to the caller and end the request.
   *
   * @param proxiedUrl Url to convert
   * @param requestCtx Context of the current request.
   * @return If succeed, a {@link URI}, otherwise {@code null}.
   */
  URI createUriFromUrl(String proxiedUrl, RoutingContext requestCtx) {
    URI uri;
    try {
      uri = URI.create(proxiedUrl);
    } catch (Exception e) {
      final String errorMsg = "Failed to parse the url [" + proxiedUrl + "] to proxy.";
      log.error(errorMsg, e);
      requestCtx.response().setStatusCode(500).end(errorMsg);
      return null;
    }
    return uri;
  }

  /**
   * Proxy the request to url {@code proxiedUrl}.
   *
   * @param proxiedUrl The url to proxy.
   * @param requestCtx Context of the current request.
   */
  void proxyRequest(URI proxiedUrl, RoutingContext requestCtx) {
    final HttpServerRequest serverRequest = requestCtx.request();
    final HttpServerResponse serverResponse = requestCtx.response();


    final String host = proxiedUrl.getHost();
    final String scheme = proxiedUrl.getScheme();
    final String path = proxiedUrl.getPath();
    int port = proxiedUrl.getPort();

    if (port == -1) {
      // if there are no port, and the scheme is https, set as 443, default https port, else set as 80, default HTTP port
      if ("https".equalsIgnoreCase(scheme)) {
        log.warn("No port specified in the url to proxy [{}], using 443 since it's a HTTPS request.", proxiedUrl);
        port = 443;
      } else {
        log.warn("No port specified in the url to proxy [{}], using 80", proxiedUrl);
        port = 80;
      }
    }

    log.info("Proxing request to {}", proxiedUrl);

    // create a request
    final HttpClient httpClient = createHttpClient(requestCtx.vertx());
    final HttpClientRequest httpClientRequest = httpClient.get(port, host, path + (proxiedUrl.getQuery() != null ? "?" + proxiedUrl.getQuery() : ""));

    // configure Basic Auth if present
    final String authorization = serverRequest.getParam("authorization");
    if (authorization != null) {
      httpClientRequest.putHeader(HttpHeaders.AUTHORIZATION, authorization);
    }

    // set the serverResponse handler
    httpClientRequest.handler(clientResponse -> {
      // response success
      if (clientResponse.statusCode() == 200) {
        serverResponse.setChunked(true);
        serverResponse.setStatusCode(200);

        // setup the headers from proxied request, ignoring the transfer encoding
        clientResponse.headers().forEach(headerEntry -> {
          if (!HttpHeaders.TRANSFER_ENCODING.equals(headerEntry.getKey()))
            serverResponse.putHeader(headerEntry.getKey(), headerEntry.getValue());
        });

        // transfer response from the proxied  to the server response
        final Pump pump = Pump.pump(clientResponse, serverResponse);

        // if there are any errors, usually connection closed, stop the pump and close the connection if it still open
        clientResponse.exceptionHandler(t -> {
          if (t instanceof VertxException && t.getMessage().equalsIgnoreCase("Connection was closed")) {
            log.info("Proxied connection stopped.");
          } else {
            log.error("Proxy response", t);
          }
          closeQuietlyHttpClient(httpClient);
        });

        // start the transferring
        pump.start();
      } else {
        log.error("Connecting to the proxied url: Status Code: {}, Status Message: {}", clientResponse.statusCode(), clientResponse.statusMessage());
        serverResponse.setStatusCode(500).end("Fail to connect to client, response code: " + clientResponse.statusCode());
        closeQuietlyHttpClient(httpClient);
      }
    });

    // handle errors on the client side (hsytrix-dashboard client)
    requestCtx.response().closeHandler(ignored -> {
      log.warn("[Client disconnected] Connection closed on client side");
      log.info("Stopping the proxing...");
      closeQuietlyHttpClient(httpClient);
    });

    // handle exception on request
    serverRequest.exceptionHandler(t -> {
      log.error("On server request", t);
      closeQuietlyHttpClient(httpClient);
    });

    // handle exceptions on proxied server side
    httpClientRequest.exceptionHandler(t -> {
      log.error("Proxying request", t);
      serverResponse.setStatusCode(500);

      if (t.getMessage() != null)
        serverResponse.end(t.getMessage());
      else
        serverResponse.end();

      closeQuietlyHttpClient(httpClient);
    });

    // request timeout
    httpClientRequest.setTimeout(5000L);

    // do the request
    httpClientRequest.end();
  }

  /**
   * If the HttpClient is already closed when you try to close it again it throws {@link IllegalStateException}, this method swallow it.
   *
   * @param client Client to close.
   */
  @SuppressWarnings({"PMD.EmptyCatchBlock"})
  private void closeQuietlyHttpClient(HttpClient client) {
    try {
      client.close();
    } catch (Exception ignored) { }
  }

  /**
   * Initialize if need and returns the {@link HttpClient} for proxying requests.
   *
   * @return The {@link HttpClient} for proxying requests.
   */
  private HttpClient createHttpClient(Vertx vertx) {
    final HttpClientOptions httpClientOptions = new HttpClientOptions().setKeepAlive(false)
                                                                       .setTryUseCompression(true)
                                                                       .setMaxPoolSize(1); // just 1 because the client will be closed when the request end

    return vertx.createHttpClient(httpClientOptions);
  }
}
