package org.example.breakoutdrop.Errors.Server;

public class ServerError500 extends RuntimeException {
    public ServerError500(String message) {
        super(message);
    }
}
