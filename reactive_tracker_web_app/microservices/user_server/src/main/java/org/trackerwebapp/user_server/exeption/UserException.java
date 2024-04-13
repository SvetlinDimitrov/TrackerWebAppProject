package org.trackerwebapp.user_server.exeption;

import lombok.Getter;

@Getter
public class UserException extends Exception {
  private final String message;

  public UserException(String message) {
    super(message);
    this.message = message;
  }
}
