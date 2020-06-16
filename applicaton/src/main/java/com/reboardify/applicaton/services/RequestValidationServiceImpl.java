package com.reboardify.applicaton.services;

import com.reboardify.applicaton.domains.Employee;
import org.apache.commons.lang.StringUtils;
import org.springframework.stereotype.Service;

@Service
public class RequestValidationServiceImpl implements RequestValidationService {

  @Override
  public Boolean isRequestValid(Employee employee) {
    if (employee == null || employee.getId() == null){
      return false;
    }
    return !employee.getId().equals("") && StringUtils
        .isNotBlank(employee.getId());
  }
}
