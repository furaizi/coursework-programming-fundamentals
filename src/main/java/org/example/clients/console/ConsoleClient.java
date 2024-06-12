package org.example.clients.console;

import org.example.dto.Message;
import org.example.dto.MessageType;
import org.example.util.Connection;

import java.io.IOException;
import java.net.Socket;

import org.example.util.ConsoleHelper;
import org.example.util.Logger;
import org.example.util.exceptions.client.ClientException;
import org.example.util.exceptions.client.CreatingSocketException;
import org.example.util.exceptions.connection.SendingMessageException;
import org.example.util.exceptions.client.UnexpectedMessageTypeException;
import static org.example.clients.ClientConstants.*;

public class ConsoleClient {

    private static Logger logger = new Logger();

    protected Connection server;
    private volatile boolean clientConnected = false;


    public static void main(String[] args) {
        ConsoleClient client = new ConsoleClient();
        client.run();
    }


    public void run() {
        var socketThread = getSocketThread();
        socketThread.setDaemon(true);
        socketThread.setUncaughtExceptionHandler((thread, e) -> logger.log(e));
        socketThread.start();

        synchronized (this) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new CreatingSocketException();
            }

            logConnectionInfo();

            while (clientConnected) {
                var text = ConsoleHelper.readString();
                if (shouldStopConnection(text))
                    break;
                if (shouldSendTextFromConsole())
                    sendTextMessage(text);
            }
            clientConnected = false;
        }
    }

    public void sendTextMessage(String text) {
        try {
            MessageType type = isPrivateTextMessage(text) ? MessageType.PRIVATE_TEXT : MessageType.GLOBAL_TEXT;
            server.send(new Message(type, text));
        } catch (SendingMessageException e) {
            clientConnected = false;
            throw e;
        }
    }

    public boolean isPrivateTextMessage(String text) {
        return text.matches("\\w+: .+"); // user: text
    }

    protected String getServerAddress() {
        ConsoleHelper.writeMessage(ENTER_SERVER_ADDRESS);
        return ConsoleHelper.readString();
    }

    protected int getServerPort() {
        ConsoleHelper.writeMessage(ENTER_SERVER_PORT);
        return ConsoleHelper.readInt();
    }

    protected String getUserName() {
        ConsoleHelper.writeMessage(ENTER_USERNAME);
        return ConsoleHelper.readString();
    }

    protected boolean shouldSendTextFromConsole() {
        return true;
    }

    protected boolean isBot() {
        return false;
    }

    protected SocketThread getSocketThread() {
        return new SocketThread();
    }

    private void logConnectionInfo() {
        if (clientConnected)
            logger.info(CONNECTION_IS_ESTABLISHED
                    + "\n" +
                    ENTER_EXIT_COMMAND);
        else
            throw new ClientException();
    }

    private boolean shouldStopConnection(String text) {
        return text == null || text.equals(EXIT_COMMAND);
    }


    public class SocketThread extends Thread {

        @Override
        public void run() {
            try {
                String address = getServerAddress();
                var port = getServerPort();
                var socket = new Socket(address, port);
                server = new Connection(socket);
                clientHandshake();
                clientMainLoop();
            } catch (IOException e) {
                notifyConnectionStatusChanged(false);
                logger.log(e);
            }
            catch (RuntimeException e) {
                notifyConnectionStatusChanged(false);
                throw e;
            }
        }

        protected void processIncomingMessage(String message) {
            ConsoleHelper.writeMessage(message);
        }

        protected void informAboutAddingNewUser(String username) {
            ConsoleHelper.writeMessage(USER_JOINED, username);
        }

        protected void informAboutDeletingNewUser(String username) {
            ConsoleHelper.writeMessage(USER_LEFT, username);
        }

        protected void notifyConnectionStatusChanged(boolean clientConnected) {
            ConsoleClient.this.clientConnected = clientConnected;
            synchronized (ConsoleClient.this) {
                ConsoleClient.this.notify();
            }
        }

        protected void clientHandshake() {
            while (true) {
                var message = server.receive();
                if (isMessageNull(message))
                    throw new UnexpectedMessageTypeException();

                switch (message.type()) {
                    case NAME_REQUEST -> {
                        var username = getUserName();
                        server.send(new Message(MessageType.USER_NAME, username));
                    }
                    case NAME_ACCEPTED -> {
                        notifyConnectionStatusChanged(true);
                        return;
                    }
                    default -> throw new UnexpectedMessageTypeException();
                }
            }
        }

        protected void clientMainLoop() {
            while (true) {
                var message = server.receive();
                if (isMessageNull(message))
                    throw new UnexpectedMessageTypeException();

                switch (message.type()) {
                    case GLOBAL_TEXT, PRIVATE_TEXT -> processIncomingMessage(message.data());
                    case USER_ADDED -> informAboutAddingNewUser(message.data());
                    case USER_REMOVED -> informAboutDeletingNewUser(message.data());
                    default -> throw new UnexpectedMessageTypeException();
                }
            }
        }

        private boolean isMessageNull(Message message) {
            return message == null || message.type() == null;
        }

    }
}
