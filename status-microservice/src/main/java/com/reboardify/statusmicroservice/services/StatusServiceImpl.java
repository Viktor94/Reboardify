package com.reboardify.statusmicroservice.services;

import com.reboardify.statusmicroservice.modells.Message;
import com.reboardify.statusmicroservice.modells.Position;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class StatusServiceImpl implements StatusService {

  @Override
  public ResponseEntity<?> checkResponse(ResponseEntity<?> response) {
    assert response != null;

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
