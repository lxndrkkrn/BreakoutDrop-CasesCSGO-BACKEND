package org.example.breakoutdrop.Errors.ClientHTTP;

public class TooManyRequests429 extends RuntimeException {
    public TooManyRequests429(String message) {
        super(message);
    }
}
