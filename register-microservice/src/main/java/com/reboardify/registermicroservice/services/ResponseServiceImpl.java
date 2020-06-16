package com.reboardify.registermicroservice.services;

import com.reboardify.registermicroservice.models.ErrorMessage;
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
   * @return It returns with a response entity containing a message with the status
   */
  @Override
  public ResponseEntity<?> checkServerResponse(ResponseEntity<?> response) {
    if (response == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    if (response.getStatusCode().is4xxClientError()) {
      return new ResponseEntity<>(new ErrorMessage("This ID is already registered!"),
          HttpStatus.OK);
    } else if (response.getStatusCode().is5xxServerError()) {
      return new ResponseEntity<>(
          new ErrorMessage("The server is not responding, please try again later."),
          HttpStatus.INTERNAL_SERVER_ERROR);
    }
    return new ResponseEntity<>(new ErrorMessage("You have registered successfully!"),
        HttpStatus.OK);
  }
}
