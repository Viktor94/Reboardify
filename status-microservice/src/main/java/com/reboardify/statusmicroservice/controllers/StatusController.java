package com.reboardify.statusmicroservice.controllers;

import com.reboardify.statusmicroservice.modells.Employee;
import com.reboardify.statusmicroservice.modells.Position;
import com.reboardify.statusmicroservice.services.StatusService;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient.Builder;

@RestController
public class StatusController {

  private final Builder webclientBuilder;

  private final StatusService statusService;

  @Autowired
  public StatusController(@Qualifier("getWebClientBuilder") Builder webclientBuilder,
      StatusService statusService) {
    this.webclientBuilder = webclientBuilder;
    this.statusService = statusService;
  }

  @PostMapping("/status")
  public ResponseEntity<?> getPosition(@RequestBody Employee employee) {
    String url = System.getenv("DB_MS_URL");

    ResponseEntity<?> response = Objects.requireNonNull(webclientBuilder.build()
        .get()
        .uri(url + employee.getId())
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .exchange()
        .block())
        .toEntity(Position.class)
        .block();

    return statusService.checkResponse(response);
  }
}
