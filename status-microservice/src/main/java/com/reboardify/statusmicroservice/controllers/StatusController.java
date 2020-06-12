package com.reboardify.statusmicroservice.controllers;

import com.reboardify.statusmicroservice.modells.Employee;
import com.reboardify.statusmicroservice.modells.Position;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient.Builder;

@RestController
public class StatusController {

  private Builder webclientBuilder;

  @Autowired
  public StatusController(@Qualifier("getWebClientBuilder") Builder webclientBuilder) {
    this.webclientBuilder = webclientBuilder;
  }

  @GetMapping("/status")
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

    assert response != null;
    Position position = (Position) response.getBody();

    if (response.getStatusCode() != HttpStatus.OK) {
      return new ResponseEntity<>(response.getStatusCode());
    }

    return new ResponseEntity<>(position, HttpStatus.OK );
  }
}
