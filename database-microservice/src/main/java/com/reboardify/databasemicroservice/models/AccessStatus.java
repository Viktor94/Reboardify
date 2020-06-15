package com.reboardify.databasemicroservice.models;

/**
 * AccessStatus is used to tell if an employee can enter or exit the office.
 */
public class AccessStatus {

  /**
   * This variable is used to store the id of the employee
   */
  private String id;
  /**
   * This variable is used to check if the employee is authorized.
   */
  private Boolean isAuthorized;

  public AccessStatus() {
  }

  public AccessStatus(String id, Boolean isAuthorized) {
    this.id = id;
    this.isAuthorized = isAuthorized;
  }

  public String getId() {
    return id;
  }

  public void setId(String id) {
    this.id = id;
  }

  public Boolean getAuthorized() {
    return isAuthorized;
  }

  public void setAuthorized(Boolean authorized) {
    isAuthorized = authorized;
  }
}
