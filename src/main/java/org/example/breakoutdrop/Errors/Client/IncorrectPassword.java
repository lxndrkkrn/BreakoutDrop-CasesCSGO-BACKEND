package org.example.breakoutdrop.Errors.Client;

public class IncorrectPassword extends RuntimeException {
  public IncorrectPassword(String message) {
    super(message);
  }
}
