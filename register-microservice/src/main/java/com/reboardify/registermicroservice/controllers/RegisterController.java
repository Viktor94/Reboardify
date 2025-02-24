package com.reboardify.registermicroservice.controllers;

import com.reboardify.registermicroservice.models.Employee;
import com.reboardify.registermicroservice.models.Message;
import com.reboardify.registermicroservice.services.ResponseService;
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

/**
 * Rest controller to process incoming http requests
 */
@RestController
public class RegisterController {

  /**
   * The builder is used to make http requests
   */
  private final Builder webclientBuilder;
  /**
   * Response service is used to process the responses
   */
  private final ResponseService responseService;

  @Autowired
  public RegisterController(
      @Qualifier("getWebclientBuilder") Builder webclientBuilder,
      ResponseService responseService) {
    this.webclientBuilder = webclientBuilder;
    this.responseService = responseService;
  }

  /**
   * <p>This method sends an http request to the database microservice
   * when the employee tries to register</p>
   * @param employee employee is used to get the id of it
   * @return It returns with a response entity containing a message with the status
   */
  @PostMapping("/register")
  public ResponseEntity<?> register(@RequestBody Employee employee) {
    String url = System.getenv("DB_MS_URL");
    try {
      ResponseEntity<?> response = webclientBuilder.build()
          .post()
          .uri(url + "/employee")
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

      return responseService.checkServerResponse(response);
    } catch (Exception e) {
      return new ResponseEntity<>(new Message("Could not connect to server, try again later."),
          HttpStatus.OK);
    }
  }
}
