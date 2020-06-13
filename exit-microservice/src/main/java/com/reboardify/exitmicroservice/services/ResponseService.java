package com.reboardify.exitmicroservice.services;

import org.springframework.http.ResponseEntity;

public interface ResponseService {

  ResponseEntity<?> checkServerResponse(ResponseEntity<?> responseEntity);
}
