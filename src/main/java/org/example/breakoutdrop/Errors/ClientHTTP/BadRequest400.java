package org.example.breakoutdrop.Errors.Client;

public class BadRequest400 extends RuntimeException {
    public BadRequest400(String message) {
        super(message);
    }
}
