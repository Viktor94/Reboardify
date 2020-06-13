package com.reboardify.registermicroservice.services;

import com.reboardify.registermicroservice.models.ErrorMessage;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ResponseServiceImpl implements ResponseService {

  @Override
  public ResponseEntity<?> checkServerResponse(ResponseEntity<?> response) {
    assert response != null;
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
