package com.reboardify.exitmicroservice.controllers;

import com.reboardify.exitmicroservice.models.Employee;
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
public class ExitController {

  private Builder webClientBuilder;

  @Autowired
  public ExitController(@Qualifier("getWebClientBuilder") Builder webClientBuilder) {
    this.webClientBuilder = webClientBuilder;
  }

  @PostMapping("/exit")
  public ResponseEntity<?> exit(@RequestBody Employee employee) {
    String url = System.getenv("DB_MS_URL");

    ResponseEntity<?> response = webClientBuilder.build()
        .post()
        .uri(url)
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .body(Mono.just(employee), Employee.class)
        .retrieve()
        .toBodilessEntity()
        .onErrorResume(WebClientResponseException.class,
            ex -> ex.getRawStatusCode() == 409 ? Mono.just(new ResponseEntity<>(HttpStatus.CONFLICT))
                : Mono.error(ex))
        .block();

        return new ResponseEntity<>(response.getStatusCode());
  }
}
