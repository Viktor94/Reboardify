package com.reboardify.statusmicroservice.services;

import org.springframework.http.ResponseEntity;

public interface ResponseService {

  ResponseEntity<?> checkResponse(ResponseEntity<?> responseEntity);
}
