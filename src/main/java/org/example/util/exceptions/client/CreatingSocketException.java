package org.example.util.exceptions.client;

public class CreatingSocketException extends ClientException {

    private static final String MESSAGE = "An error occurred while creating a socket.";

    public CreatingSocketException(String message) {
        super(message);
    }

    public CreatingSocketException() {
        super(MESSAGE);
    }
}
