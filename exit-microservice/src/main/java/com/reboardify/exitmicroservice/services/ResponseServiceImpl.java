package com.reboardify.exitmicroservice.services;

import com.reboardify.exitmicroservice.models.EntryStatus;
import com.reboardify.exitmicroservice.models.Message;
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
    if (response.getStatusCode().is5xxServerError()) {
      return new ResponseEntity<>(
          new Message("The server is not responding, please try again later."),
          HttpStatus.OK);
    }
    EntryStatus entryStatus = (EntryStatus) response.getBody();
    assert entryStatus != null;
    if (!entryStatus.getAuthorized()) {
      return new ResponseEntity<>(
          new Message(
              "Employee: " + entryStatus.getId() + " tried to exit without entering the office."),
          HttpStatus.OK);
    }
    return new ResponseEntity<>(
        new Message("Employee: " + entryStatus.getId() + " has left."),
        HttpStatus.OK);
  }
}
