package com.reboardify.servicediscovery;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * Class to discover applications on Eureka Server
 */
@SpringBootApplication
@EnableEurekaServer
public class ServiceDiscoveryApplication {

  public static void main(String[] args) {
    SpringApplication.run(ServiceDiscoveryApplication.class, args);
  }
}
