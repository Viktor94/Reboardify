package com.reboardify.entrymicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.web.reactive.function.client.WebClient;

@SpringBootApplication
public class EntryMicroserviceApplication {

  @Bean
  public WebClient.Builder getWebclientBuilder() {
    return WebClient.builder();
  }

  public static void main(String[] args) {
    SpringApplication.run(EntryMicroserviceApplication.class, args);
  }

}
