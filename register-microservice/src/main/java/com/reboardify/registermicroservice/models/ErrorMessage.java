package com.reboardify.registermicroservice.models;

/**
 * Message is used to send response in the body.
 */
public class ErrorMessage {

  /**
   * This variable is used to store the message.
   */
  private String message;

  public ErrorMessage(String message) {
    this.message = message;
  }

  public String getMessage() {
    return message;
  }

  public void setMessage(String message) {
    this.message = message;
  }
}
