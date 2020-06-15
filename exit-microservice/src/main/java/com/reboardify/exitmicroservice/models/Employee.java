package com.reboardify.exitmicroservice.models;

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

  public Employee(String id) {
    this.id = id;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
