package com.reboardify.entrymicroservice.controllers;

import com.reboardify.entrymicroservice.Service.ResponseService;
import com.reboardify.entrymicroservice.models.Employee;
import com.reboardify.entrymicroservice.models.EntryStatus;
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
public class EntryController {

  /**
   * The builder is used to make http requests
   */
  private final Builder webclientBuilder;
  /**
   * Response service is used to process the responses
   */
  private final ResponseService responseService;

  @Autowired
  public EntryController(
      @Qualifier("getWebclientBuilder") Builder webclientBuilder,
      ResponseService responseService) {
    this.webclientBuilder = webclientBuilder;
    this.responseService = responseService;
  }

  /**
   * <p>This method is used to create an http request to the database microservice</p>
   *
   * @param employee employee is used to get the id of it
   * @return It returns with a response entity containing a message with the status
   */
  @PostMapping("/entry")
  public ResponseEntity<?> entry(@RequestBody Employee employee) {
    String url = System.getenv("DB_MS_URL");

    ResponseEntity<?> response = webclientBuilder.build()
        .get()
        .uri(url + "/entry/" + employee.getId())
        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
        .retrieve()
        .toEntity(EntryStatus.class)
        .block();

    return responseService.checkServerResponse(response);
  }
}
