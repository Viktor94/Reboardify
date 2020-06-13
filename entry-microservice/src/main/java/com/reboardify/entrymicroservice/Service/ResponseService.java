package com.reboardify.entrymicroservice.Service;

import org.springframework.http.ResponseEntity;

public interface ResponseService {

  ResponseEntity<?> checkServerResponse(ResponseEntity<?> responseEntity);
}
