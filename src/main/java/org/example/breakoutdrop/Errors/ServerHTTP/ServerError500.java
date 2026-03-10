package org.example.breakoutdrop.Errors.ServerHTTP;

public class ServerError500 extends RuntimeException {
    public ServerError500(String message) {
        super(message);
    }
}
