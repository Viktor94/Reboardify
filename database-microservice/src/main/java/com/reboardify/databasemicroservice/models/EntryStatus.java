package com.reboardify.databasemicroservice.models;

public class EntryStatus {

  String id;
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
