package com.reboardify.applicaton.services;

import com.reboardify.applicaton.domains.Employee;
import com.reboardify.applicaton.domains.Message;
import org.apache.commons.lang.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

public class RequestValidationServiceImpl implements RequestValidationService {

  @Override
  public ResponseEntity<?> validateRequest(Employee employee) {
    if (employee == null || employee.getId().equals("") || StringUtils
        .isNotBlank(employee.getId())) {
      return new ResponseEntity<>(new Message("User ID is missing!"), HttpStatus.BAD_REQUEST);
    }

    return null;
  }
}
