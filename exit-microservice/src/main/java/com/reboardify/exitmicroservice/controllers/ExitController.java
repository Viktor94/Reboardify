package com.reboardify.exitmicroservice.controllers;

import com.reboardify.exitmicroservice.models.Employee;
import com.reboardify.exitmicroservice.models.EntryStatus;
import com.reboardify.exitmicroservice.services.ResponseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import reactor.core.publisher.Mono;

@RestController
public class ExitController {

  private final Builder webClientBuilder;
  private final ResponseService responseService;

  @Autowired
  public ExitController(@Qualifier("getWebClientBuilder") Builder webClientBuilder,
      ResponseService responseService) {
    this.webClientBuilder = webClientBuilder;
    this.responseService = responseService;
  }

  @PostMapping("/exit")
  public ResponseEntity<?> exit(@RequestBody Employee employee) {
    String url = System.getenv("DB_MS_URL");

    ResponseEntity<?> response = webClientBuilder.build()
        .post()
        .uri(url + "/exit")
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .body(Mono.just(employee), Employee.class)
        .retrieve()
        .toEntity(EntryStatus.class)
        .block();

    return responseService.checkServerResponse(response);
  }
}
