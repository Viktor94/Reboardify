package com.reboardify.registermicroservice.services;

import org.springframework.http.ResponseEntity;

public interface ResponseService {

  ResponseEntity checkServerResponse(ResponseEntity responseEntity);

}
