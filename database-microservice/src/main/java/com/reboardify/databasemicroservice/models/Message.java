package com.reboardify.databasemicroservice.models;

/**
 * Message is used to send response in the body.
 */
public class Message {

  /**
   * This variable is used to store the message.
   */
  private String message;

  public Message(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
