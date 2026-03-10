package org.example.breakoutdrop.Errors.Client;

public class IncorrectEmail extends RuntimeException {
  public IncorrectEmail(String message) {
    super(message);
  }
}
