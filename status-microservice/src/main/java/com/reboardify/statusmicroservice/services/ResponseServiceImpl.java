package com.reboardify.statusmicroservice.services;

import com.reboardify.statusmicroservice.modells.Message;
import com.reboardify.statusmicroservice.modells.Position;
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
  public ResponseEntity<?> checkResponse(ResponseEntity<?> response) {
    if (response == null) {
      return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }
    Position position = (Position) response.getBody();

    assert position != null;
    if (position.getPosition() == 0) {
      return new ResponseEntity<>(new Message("You can enter!"),
          HttpStatus.OK);
    } else if (position.getPosition() > 0) {
      return new ResponseEntity<>(
          new Message("Your position in the queue: " + position.getPosition()),
          HttpStatus.OK);
    }
    return new ResponseEntity<>(new Message("You are not registered!"),
        HttpStatus.OK);
  }
}
