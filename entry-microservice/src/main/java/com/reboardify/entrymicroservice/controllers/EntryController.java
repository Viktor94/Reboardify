package com.reboardify.entrymicroservice.controllers;

import com.reboardify.entrymicroservice.models.Employee;
import com.reboardify.entrymicroservice.models.ErrorMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;

@RestController
public class EntryController {

  private Builder webclientBuilder;

  @Autowired
  public EntryController(
      @Qualifier("getWebclientBuilder") Builder webclientBuilder) {
    this.webclientBuilder = webclientBuilder;
  }

  @PostMapping("/entry")
  public ResponseEntity<?> entry(@RequestBody Employee employee) {
    String url = System.getenv("DB_MS_URL");

    try {
      ResponseEntity response = webclientBuilder.build()
          .post()
          .uri(url + "/entry")
          .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
          .body(Mono.just(employee), Employee.class)
          .retrieve()
          .toBodilessEntity()
          .onErrorResume(WebClientResponseException.class,
              ex -> ex.getRawStatusCode() == 409 ? Mono
                  .just(new ResponseEntity<>(HttpStatus.CONFLICT)) : Mono.error(ex))
          .onErrorResume(WebClientResponseException.class,
              ex -> ex.getRawStatusCode() == 500 ? Mono
                  .just(new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR)) : Mono.error(ex))
          .block();

      assert response != null;
      return new ResponseEntity<>(response.getBody(), response.getStatusCode());

    } catch (Exception e) {
      return new ResponseEntity<>(new ErrorMessage("Could not connect to server!"),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
  }


}
