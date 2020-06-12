package com.reboardify.databasemicroservice.models;

import java.util.UUID;

public class Employee {

  private String name;
  private String id;

  public Employee() {
  }

  public Employee(String name) {
    this.name = name;
    this.id = UUID.randomUUID().toString();
  }

  public Employee(String name, String id) {
    this.name = name;
    this.id = id;
  }

  public String getName() {
    return name;
  }

  public void setName(String name) {
    this.name = name;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }
}
