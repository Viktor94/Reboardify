package com.reboardify.databasemicroservice.models;

/**
 * Position is used to send back the position of an employee in the que list. Position = 0 The
 * employee can enter Position = -1 The employee is not yet registered
 */
public class Position {

  /**
   * This variable is used to store the position of the employee.
   */
  private Integer position;

  public Position() {
  }

  public Position(Integer position) {
    this.position = position;
  }

  public Integer getPosition() {
    return position;
  }

  public void setPosition(Integer position) {
    this.position = position;
  }
}
