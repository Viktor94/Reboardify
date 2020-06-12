package com.reboardify.registermicroservice.controllers;

import com.reboardify.registermicroservice.models.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@RestController
public class RegisterController {

  @Qualifier("getWebclientBuilder")
  @Autowired
  private Builder webclientBuilder;

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody Employee employee) {
    String url = System.getenv("DB_MS_URL");

    return webclientBuilder.build()
        .post()
        .uri("http://localhost:8081/employee")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .body(Mono.just(employee), Employee.class)
        .retrieve()
        .toBodilessEntity()
        .onErrorResume(WebClientResponseException.class,
            ex -> ex.getRawStatusCode() == 400 ? Mono.empty() : Mono.error(ex))
        .block();
  }
}