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

/**
 * Rest controller to process incoming http requests
 */
@RestController
public class ExitController {

  /**
   * The builder is used to make http requests
   */
  private final Builder webClientBuilder;
  /**
   * Response service is used to process the responses
   */
  private final ResponseService responseService;

  @Autowired
  public ExitController(@Qualifier("getWebClientBuilder") Builder webClientBuilder,
      ResponseService responseService) {
    this.webClientBuilder = webClientBuilder;
    this.responseService = responseService;
  }

  /**
   * <p>This method sends an http request to the database microservice
   * when the employee tries to leave</p>
   */
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
