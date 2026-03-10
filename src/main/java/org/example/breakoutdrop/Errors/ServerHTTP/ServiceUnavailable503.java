package org.example.breakoutdrop.Errors.Server;

public class ServiceUnavailable503 extends RuntimeException {
    public ServiceUnavailable503(String message) {
        super(message);
    }
}
