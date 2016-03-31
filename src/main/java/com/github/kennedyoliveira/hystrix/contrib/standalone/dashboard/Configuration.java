package com.github.kennedyoliveira.hystrix.contrib.standalone.dashboard;

/**
 * Simple server configurations.
 *
 * @author Kennedy Oliveira
 */
public final class Configuration {

  /**
   * Server port to listen to, the default is {@code 7979}.
   */
  public static final Integer SERVER_PORT = Integer.getInteger("serverPort", 7979);

  /**
   * Server address to bind, the default is {@code 0.0.0.0}
   */
  public static final String BIND_ADDRESS = System.getProperty("bindAddress", "0.0.0.0"); //NOPMD

  /**
   * Flag for disabling compression on the server, the default is {@link Boolean#TRUE}.
   */
  public static final boolean DISABLE_COMPRESSION = Boolean.getBoolean("disableCompression");

  /**
   * Number of instances, the default is only 1.
   */
  public static final Integer INSTANCES = Integer.getInteger("instances", 1);

  /**
   * Utility class, no instances for you!
   */
  private Configuration() {}
}
