package util.exceptions.client;

public class UnexpectedMessageTypeException extends ClientException {
    private static final String MESSAGE = "Unexpected message type.";

    public UnexpectedMessageTypeException() {
        super(MESSAGE);
    }
}
