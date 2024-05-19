package util.exceptions.connection;

import java.net.SocketAddress;

public class SendingMessageException extends ConnectionException {


    private static final String message = "Sending message error.";
    private static final String messageTemplate = "Sending message error. (%s)";
    private SocketAddress address;
    public SendingMessageException() {
        super(message);
    }

    public SendingMessageException(SocketAddress address) {
        super(String.format(messageTemplate, address.toString()));
        this.address = address;
    }
}
