package com.reboardify.statusmicroservice.services;

import com.reboardify.statusmicroservice.modells.Message;
import com.reboardify.statusmicroservice.modells.Position;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseServiceImplTest {

  private final ResponseService responseService = new ResponseServiceImpl();
  private ResponseEntity<?> responseEntity;

  @Test
  public void checkResponse_authorized_status() {
    Position position = new Position(0);
    responseEntity = new ResponseEntity<>(position, HttpStatus.OK);
    Message result = (Message) responseService.checkResponse(responseEntity)
        .getBody();
    Message errorMessage = new Message("You can enter!");

    Assertions.assertEquals(errorMessage.getMessage(), result.getMessage());
  }

  @Test
  public void checkResponse_unauthorized_status() {
    Position position = new Position(-1);
    responseEntity = new ResponseEntity<>(position, HttpStatus.OK);
    Message result = (Message) responseService.checkResponse(responseEntity)
        .getBody();
    Message errorMessage = new Message("You are not registered!");

    Assertions.assertEquals(errorMessage.getMessage(), result.getMessage());
  }

  @Test
  public void checkResponse_position1_status() {
    Position position = new Position(1);
    responseEntity = new ResponseEntity<>(position, HttpStatus.OK);
    Message result = (Message) responseService.checkResponse(responseEntity)
        .getBody();
    Message errorMessage = new Message("Your position in the queue: " + position.getPosition());

    Assertions.assertEquals(errorMessage.getMessage(), result.getMessage());
  }

  @Test
  public void checkResponse_badRequest() {
    responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    ResponseEntity<?> result = responseService.checkResponse(null);

    Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
  }
}
