package com.reboardify.applicaton.controllers;

import com.reboardify.applicaton.domains.Employee;
import com.reboardify.applicaton.domains.Message;
import com.reboardify.applicaton.services.RequestValidationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient.Builder;
import reactor.core.publisher.Mono;

/**
 * The Application Controller is used to send requests to the Eureka server
 */
@RestController
public class ApplicationController {

  /**
   * The Logger is used to make logs on the console
   */
  private final Logger logger = LoggerFactory.getLogger(ApplicationController.class);
  /**
   * The builder is used to make http requests
   */
  private final Builder webclientBuilder;
  /**
   * RequestValidationService is used to check the parameter of the http request
   */
  private final RequestValidationService requestValidationService;

  @Autowired
  public ApplicationController(@Qualifier("getWebclientBuilder") Builder webclientBuilder,
      RequestValidationService requestValidationService) {
    this.webclientBuilder = webclientBuilder;
    this.requestValidationService = requestValidationService;
  }

  /**
   * <p>This method adds the employee to the authorized or to the queue list.</p>
   *
   * @param employee contains the id of the employee
   * @return It returns a response entity after the registration
   */
  @PostMapping("/register")
  public ResponseEntity<?> invokeRegisterService(@RequestBody Employee employee) {
    String url = "http://REGISTER-SERVICE/register";
    logger.info("User: " + employee.getId() + " attempted to register.");

    return sendRequest(url, employee);
  }

  /**
   * <p>This method gets the position of an employee</p>
   *
   * @param employee contains the id of the employee
   * @return it returns with a response entity containing a message
   */
  @GetMapping("/status")
  public ResponseEntity<?> invokeStatusService(@RequestBody Employee employee) {
    String url = "http://STATUS-SERVICE/status";
    logger.info("User: " + employee.getId() + " requested position.");

    return sendRequest(url, employee);
  }

  /**
   * <p>This method lets in the employee if it is authorized to enter</p>
   *
   * @param employee employee is used to get the id of it
   * @return It returns with a response entity containing a message with the status
   */
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

  /**
   * <p>This method lets the employee out if it was on the authorized list</p>
   *
   * @param employee employee is used to get the id of it
   * @return It returns with a response entity containing a message with the status
   */
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

  /**
   * <p>This method is used to make http requests</p>
   *
   * @param employee employee is used to get the id of it
   * @param url      the URL of the eureka client
   * @return It returns with a response entity
   */
  private ResponseEntity<?> sendRequest(String url, Employee employee) {
    if (requestValidationService.isRequestValid(employee)) {
      return webclientBuilder.build()
          .post()
          .uri(url)
          .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
          .body(Mono.just(employee), Employee.class)
          .retrieve()
          .toEntity(Message.class)
          .block();
    }
    return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
  }
}
