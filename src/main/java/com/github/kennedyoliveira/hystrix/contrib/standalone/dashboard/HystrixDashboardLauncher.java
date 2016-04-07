package com.github.kennedyoliveira.hystrix.contrib.standalone.dashboard;

import io.vertx.core.DeploymentOptions;
import io.vertx.core.Launcher;
import io.vertx.core.Vertx;
import io.vertx.core.logging.SLF4JLogDelegateFactory;
import lombok.extern.slf4j.Slf4j;

/**
 * Custom launcher for HystrixDashboard
 *
 * @author Kennedy Oliveira
 */
@Slf4j
public class HystrixDashboardLauncher extends Launcher {

  /**
   * Main Entry-Point, can be runned from IDE too.
   *
   * @param args command line args.
   */
  public static void main(String[] args) {
    System.setProperty("vertx.logger-delegate-factory-class-name", SLF4JLogDelegateFactory.class.getName());
    new HystrixDashboardLauncher().dispatch(args);
  }

  @Override
  public void handleDeployFailed(Vertx vertx, String mainVerticle, DeploymentOptions deploymentOptions, Throwable cause) {
    log.error("Deploying verticle " + mainVerticle, cause);
    super.handleDeployFailed(vertx, mainVerticle, deploymentOptions, cause);
  }
}
