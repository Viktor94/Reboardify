package com.reboardify.registermicroservice.controllers;

import com.reboardify.registermicroservice.models.Employee;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import reactor.core.publisher.Mono;

@RestController
public class RegisterController {

  @Qualifier("getWebclientBuilder")
  @Autowired
  private Builder webclientBuilder;

  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody Employee employee) {
    String url = System.getenv("DB_MS_URL");
    ResponseEntity responseEntity = webclientBuilder.build()
        .post()
        .uri("http://localhost:8082/employee")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .body(Mono.just(employee), Employee.class)
        .retrieve()
        .bodyToMono(
            new ParameterizedTypeReference<ResponseEntity>() {
            })
        .block();
    return new ResponseEntity<>(responseEntity, HttpStatus.OK);
  }
}
