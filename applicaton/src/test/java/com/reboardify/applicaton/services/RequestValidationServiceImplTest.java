package com.reboardify.applicaton.services;

import com.reboardify.applicaton.domains.Employee;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

public class RequestValidationServiceImplTest {

  private final RequestValidationService requestValidationService = new RequestValidationServiceImpl();

  @Test
  public void assertThatEmployeeIsValid() {
    Employee employee = new Employee("123");

    Assertions.assertTrue(requestValidationService.isRequestValid(employee));
  }

  @Test
  public void assertThatEmployeeIsNull() {

    Assertions.assertFalse(requestValidationService.isRequestValid(null));
  }

  @Test
  public void assertThatEmployeeWithEmptyId() {

    Assertions.assertFalse(requestValidationService.isRequestValid(new Employee("  ")));
  }
}
