package com.reboardify.exitmicroservice.services;

import com.reboardify.exitmicroservice.models.EntryStatus;
import com.reboardify.exitmicroservice.models.Message;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseServiceImplTest {

  private final ResponseService responseService = new ResponseServiceImpl();
  private ResponseEntity<?> responseEntity;

  @Test
  public void checkResponse_badRequest() {
    responseEntity = new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    ResponseEntity<?> result = responseService.checkServerResponse(null);

    Assertions.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
  }

  @Test
  public void checkServerResponseWithAuthorizedOK() {
    responseEntity = new ResponseEntity<>(new EntryStatus("123", true), HttpStatus.OK);
    Message result = (Message) responseService.checkServerResponse(responseEntity)
        .getBody();
    Message message = new Message("Employee: 123 has left.");

    Assertions.assertEquals(message.getMessage(), result.getMessage());
  }

  @Test
  public void checkServerResponseWithUnauthorizedOK() {
    responseEntity = new ResponseEntity<>(new EntryStatus("123", false), HttpStatus.OK);
    Message result = (Message) responseService.checkServerResponse(responseEntity)
        .getBody();
    Message message = new Message("Employee: 123 tried to exit without entering the office.");

    Assertions.assertEquals(message.getMessage(), result.getMessage());
  }

  @Test
  public void checkServerResponseWithServerError() {
    responseEntity = new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
    Message result = (Message) responseService.checkServerResponse(responseEntity)
        .getBody();
    Message message = new Message("The server is not responding, please try again later.");

    Assertions.assertEquals(message.getMessage(), result.getMessage());
  }
}