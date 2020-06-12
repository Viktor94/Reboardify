package com.reboardify.databasemicroservice.models;

import java.util.UUID;

public class Employee {

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
