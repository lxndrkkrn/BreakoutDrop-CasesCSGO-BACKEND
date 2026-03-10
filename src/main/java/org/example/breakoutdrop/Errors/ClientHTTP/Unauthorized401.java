package org.example.breakoutdrop.Errors.ClientHTTP;

public class Unauthorized401 extends RuntimeException {
    public Unauthorized401(String message) {
        super(message);
    }
}
