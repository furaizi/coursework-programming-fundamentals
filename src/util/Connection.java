package util;

import dto.Message;
import util.exceptions.connection.EstablishingConnectionException;
import util.exceptions.connection.ReceivingMessageException;
import util.exceptions.connection.SendingMessageException;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.SocketAddress;

public class Connection implements AutoCloseable {
    private final Socket socket;
    private final ObjectOutputStream out;
    private final ObjectInputStream in;

    public Connection(Socket socket) {
        try {
            this.socket = socket;
            out = new ObjectOutputStream(socket.getOutputStream());
            in = new ObjectInputStream(socket.getInputStream());
        }
        catch (IOException e) {
            throw new EstablishingConnectionException(e);
        }
    }

    public void send(Message message) {
        try {
            synchronized (out) {
                out.writeObject(message);
            }
        }
        catch (IOException e) {
            throw new SendingMessageException();
        }
    }

    public Message receive() {
        try {
            synchronized (in) {
                return (Message) in.readObject();
            }
        }
        catch (IOException | ClassNotFoundException e) {
            throw new ReceivingMessageException(e);
        }
    }

    public SocketAddress getRemoteSocketAddress() {
        return socket.getRemoteSocketAddress();
    }

    public void close() {
        try {
            in.close();
            out.close();
            socket.close();
        }
        catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
