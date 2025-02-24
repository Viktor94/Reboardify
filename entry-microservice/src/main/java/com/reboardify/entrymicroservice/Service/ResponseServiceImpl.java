package com.reboardify.entrymicroservice.Service;

import com.reboardify.entrymicroservice.models.EntryStatus;
import com.reboardify.entrymicroservice.models.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

/**
 * This service contains the business logic for checking the server responses.
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
    if (response.getStatusCode().is5xxServerError()) {
      return new ResponseEntity<>(
          new Message("The server is not responding, please try again later."),
          HttpStatus.OK);
    }
    EntryStatus entryStatus = (EntryStatus) response.getBody();
    if (entryStatus == null) {
      return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    }
    if (!entryStatus.getAuthorized()) {
      return new ResponseEntity<>(
          new Message("Employee: " + entryStatus.getId() + " was unauthorized to enter."),
          HttpStatus.OK);
    }
    return new ResponseEntity<>(
        new Message("Employee: " + entryStatus.getId() + " has successfully entered."),
        HttpStatus.OK);
  }
}
