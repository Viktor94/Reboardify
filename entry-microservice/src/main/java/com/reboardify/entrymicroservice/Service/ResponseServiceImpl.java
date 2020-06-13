package com.reboardify.entrymicroservice.Service;

import com.reboardify.entrymicroservice.models.EntryStatus;
import com.reboardify.entrymicroservice.models.Message;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class ResponseServiceImpl implements ResponseService {

  @Override
  public ResponseEntity<?> checkServerResponse(ResponseEntity<?> response) {
    assert response != null;

    EntryStatus entryStatus = (EntryStatus) response.getBody();
    assert entryStatus != null;
    if (!entryStatus.getAuthorized()) {
      return new ResponseEntity<>(
          new Message("Employee: " + entryStatus.getId() + " was unauthorized to enter."),
          HttpStatus.OK);
    } else if (response.getStatusCode().is5xxServerError()) {
      return new ResponseEntity<>(
          new Message("The server is not responding, please try again later."),
          HttpStatus.OK);
    }
    return new ResponseEntity<>(
        new Message("Employee: " + entryStatus.getId() + " has successfully entered."),
        HttpStatus.OK);
  }
}
