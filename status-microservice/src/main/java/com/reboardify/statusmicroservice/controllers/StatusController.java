package com.reboardify.statusmicroservice.controllers;

import com.reboardify.statusmicroservice.modells.Employee;
import com.reboardify.statusmicroservice.modells.Position;
import com.reboardify.statusmicroservice.services.ResponseService;
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

/**
 * Rest controller to process incoming http requests
 */
@RestController
public class StatusController {

  /**
   * The builder is used to make http requests
   */
  private final Builder webclientBuilder;
  /**
   * Response service is used to process the responses
   */
  private final ResponseService responseService;

  @Autowired
  public StatusController(@Qualifier("getWebClientBuilder") Builder webclientBuilder,
      ResponseService responseService) {
    this.webclientBuilder = webclientBuilder;
    this.responseService = responseService;
  }

  /**
   * <p>This method sends an http request to the database microservice
   * when the employee tries to get the status</p>
   *
   * @return it returns with a response entity containing a message
   */
  @PostMapping("/status")
  public ResponseEntity<?> getPosition(@RequestBody Employee employee) {
    String url = System.getenv("DB_MS_URL");

    ResponseEntity<?> response = Objects.requireNonNull(webclientBuilder.build()
        .get()
        .uri(url + "/position/" + employee.getId())
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .exchange()
        .block())
        .toEntity(Position.class)
        .block();

    return responseService.checkResponse(response);
  }
}
