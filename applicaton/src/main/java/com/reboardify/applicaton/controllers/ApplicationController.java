package com.reboardify.applicaton.controllers;

import com.reboardify.applicaton.domains.Employee;
import com.reboardify.applicaton.domains.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import reactor.core.publisher.Mono;

@RestController
public class ApplicationController {

  private final Builder webclientBuilder;

  @Autowired
  public ApplicationController(@Qualifier("getWebclientBuilder") Builder webclientBuilder) {
    this.webclientBuilder = webclientBuilder;
  }

  @PostMapping("/register")
  public ResponseEntity<?> invokeRegisterService(@RequestBody Employee employee) {
    String url = "http://REGISTER-SERVICE/register";

    return webclientBuilder.build()
        .post()
        .uri(url)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .body(Mono.just(employee), Employee.class)
        .retrieve()
        .toEntity(Message.class)
        .block();
  }

  @GetMapping("/status")
  public ResponseEntity<?> invokeStatusService(@RequestBody Employee employee) {
    String url = "http://STATUS-SERVICE/status";

    return webclientBuilder.build()
        .post()
        .uri(url)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .body(Mono.just(employee), Employee.class)
        .retrieve()
        .toEntity(Message.class)
        .block();
  }

  @GetMapping("/entry")
  public ResponseEntity<?> invokeEntryService(@RequestBody Employee employee) {
    String url = "http://ENTRY-SERVICE/entry";

    return webclientBuilder.build()
        .post()
        .uri(url)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .body(Mono.just(employee), Employee.class)
        .retrieve()
        .toEntity(Message.class)
        .block();
  }

  @PostMapping("/exit")
  public ResponseEntity<?> invokeExitService(@RequestBody Employee employee) {
    String url = "http://EXIT-SERVICE/exit";

    return webclientBuilder.build()
        .post()
        .uri(url)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .body(Mono.just(employee), Employee.class)
        .retrieve()
        .toEntity(Message.class)
        .block();
  }
}
