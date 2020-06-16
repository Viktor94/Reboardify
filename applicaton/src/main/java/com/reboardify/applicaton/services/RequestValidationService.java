package com.reboardify.applicaton.services;

import com.reboardify.applicaton.domains.Employee;
import org.springframework.http.ResponseEntity;

public interface RequestValidationService {

  Boolean isRequestValid(Employee employee);
}
