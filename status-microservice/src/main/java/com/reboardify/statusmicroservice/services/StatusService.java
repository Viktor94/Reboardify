package com.reboardify.statusmicroservice.services;

import org.springframework.http.ResponseEntity;

public interface StatusService {

  ResponseEntity<?> checkResponse(ResponseEntity<?> responseEntity);
}
