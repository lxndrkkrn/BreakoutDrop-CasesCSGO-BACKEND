package org.example.breakoutdrop.Errors.Client;

public class TooManyRequests429 extends RuntimeException {
    public TooManyRequests429(String message) {
        super(message);
    }
}
