package org.example.breakoutdrop.Errors.Client;

public class NegativeBalance extends RuntimeException {
  public NegativeBalance(String message) {
    super(message);
  }
}
