package com.reboardify.exitmicroservice.models;

/**
 * EntryStatus is used to tell if an employee can enter or exit the office.
 */
public class EntryStatus {

  /**
   * This variable is used to store the id of the employee
   */
  private String id;
  /**
   * This variable is used to check if the employee is authorized.
   */
  Boolean isAuthorized;

  public EntryStatus() {
  }

  public EntryStatus(String id, Boolean isAuthorized) {
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
