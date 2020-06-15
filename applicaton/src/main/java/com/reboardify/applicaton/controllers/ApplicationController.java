package com.reboardify.applicaton.controllers;

import com.reboardify.applicaton.domains.Employee;
import com.reboardify.applicaton.domains.Message;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

  private final Logger logger = LoggerFactory.getLogger(ApplicationController.class);
  private final Builder webclientBuilder;

  @Autowired
  public ApplicationController(@Qualifier("getWebclientBuilder") Builder webclientBuilder) {
    this.webclientBuilder = webclientBuilder;
  }

  @PostMapping("/register")
  public ResponseEntity<?> invokeRegisterService(@RequestBody Employee employee) {
    String url = "http://REGISTER-SERVICE/register";
    logger.info("User: " + employee.getId() + " attempted to register.");

    return sendRequest(url, employee);
  }

  @GetMapping("/status")
  public ResponseEntity<?> invokeStatusService(@RequestBody Employee employee) {
    String url = "http://STATUS-SERVICE/status";
    logger.info("User: " + employee.getId() + " requested position.");

    return sendRequest(url, employee);
  }

  @GetMapping("/entry")
  public ResponseEntity<?> invokeEntryService(@RequestBody Employee employee) {
    String url = "http://ENTRY-SERVICE/entry";
    logger.info("User: " + employee.getId() + " attempted to enter.");
    ResponseEntity<?> responseEntity = sendRequest(url, employee);
    Message message = (Message) responseEntity.getBody();
    if (message != null) {
      logger.info(message.getMessage());
    }
    return responseEntity;
  }

  @PostMapping("/exit")
  public ResponseEntity<?> invokeExitService(@RequestBody Employee employee) {
    String url = "http://EXIT-SERVICE/exit";
    logger.info("User: " + employee.getId() + " attempted to exit.");
    ResponseEntity<?> responseEntity = sendRequest(url, employee);
    Message message = (Message) responseEntity.getBody();
    if (message != null) {
      logger.info(message.getMessage());
    }
    return responseEntity;
  }

  private ResponseEntity<?> sendRequest(String url, Employee employee) {
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
