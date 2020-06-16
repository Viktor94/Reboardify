package com.reboardify.registermicroservice.services;

import com.reboardify.registermicroservice.models.Message;
import org.junit.Assert;
import org.junit.Test;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class ResponseServiceImplTest {

  private final ResponseService responseService = new ResponseServiceImpl();
  private ResponseEntity<?> responseEntity;

  @Test
  public void checkServerResponseWithOk() {
    responseEntity = new ResponseEntity<>(HttpStatus.OK);
    Message result = (Message) responseService.checkServerResponse(responseEntity)
        .getBody();
    Message message = new Message("You have registered successfully!");

    Assert.assertEquals(message.getMessage(), result.getMessage());
  }

  @Test
  public void checkServerResponseWithNull() {
    responseEntity = new ResponseEntity<>(HttpStatus.OK);
    ResponseEntity<?> result = responseService.checkServerResponse(null);

    Assert.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
  }
}