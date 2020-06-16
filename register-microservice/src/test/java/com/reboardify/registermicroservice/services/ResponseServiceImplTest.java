package com.reboardify.registermicroservice.services;

import com.reboardify.registermicroservice.models.ErrorMessage;
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
    ErrorMessage result = (ErrorMessage) responseService.checkServerResponse(responseEntity)
        .getBody();
    ErrorMessage errorMessage = new ErrorMessage("You have registered successfully!");

    Assert.assertEquals(errorMessage.getMessage(), result.getMessage());
  }

  @Test
  public void checkServerResponseWithNull() {
    responseEntity = new ResponseEntity<>(HttpStatus.OK);
    ResponseEntity<?> result = responseService.checkServerResponse(null);

    Assert.assertEquals(HttpStatus.BAD_REQUEST, result.getStatusCode());
  }
}