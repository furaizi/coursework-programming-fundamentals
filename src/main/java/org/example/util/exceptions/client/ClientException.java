package org.example.util.exceptions.client;

public class ClientException extends RuntimeException {

    private static final String MESSAGE = "An error has occurred while the client was running.";
    public ClientException(String message) {
        super(message);
    }

    public ClientException() {
        super(MESSAGE);
    }
}
