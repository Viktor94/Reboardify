package com.reboardify.statusmicroservice.modells;

/**
 * Employee is used to create an employee object from an id.
 */
public class Employee {

  /**
   * This variable is used to store the id of the employee.
   */
  private String id;

  public Employee() {
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
