package com.reboardify.registermicroservice.services;

import com.reboardify.registermicroservice.models.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * ResponseServiceImpl handles server responses
 */
@Service
public class ResponseServiceImpl implements ResponseService {

  /**
   * <p>This method is used to process the response from the database microservice</p>
   *
   * @param response The response entity from the microservice
   * @return It returns with a response entity containing a message
   */
  @Override
  public ResponseEntity<?> checkServerResponse(ResponseEntity<?> response) {
    if (response == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    if (response.getStatusCode().is4xxClientError()) {
      return new ResponseEntity<>(new Message("This ID is already registered!"),
          HttpStatus.OK);
    } else if (response.getStatusCode().is5xxServerError()) {
      return new ResponseEntity<>(
          new Message("The server is not responding, please try again later."),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<>(new Message("You have registered successfully!"),
        HttpStatus.OK);
  }
}
