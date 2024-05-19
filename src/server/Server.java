package server;

import dto.Message;
import dto.MessageType;
import util.Connection;
import util.ConsoleHelper;
import util.Logger;
import util.exceptions.MessageIsNotTextException;
import util.exceptions.connection.SendingMessageException;

import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class Server {

    private static final String SERVER_IS_ON = "Server is ON";
    private static final String CONNECTION_IS_ESTABLISHED = "A new connection with remote address is established. (%s)";
    private static final String CONNECTION_IS_CLOSED = "A connection with remote address is closed. (%s)";
    private static final Logger logger = new Logger();

    private static Map<String, Connection> connectionMap = new ConcurrentHashMap<>();


    public static void main(String[] args) throws Exception {
        var port = ConsoleHelper.readInt();
        List<Handler> handlersList = new ArrayList<>();

        try (var serverSocket = new ServerSocket(port)) {
            logger.info(SERVER_IS_ON);

            while (true) {
                var handler = new Handler(serverSocket.accept());
                handler.setUncaughtExceptionHandler((thread, e) -> logger.log(e));
                handlersList.add(handler);
                handler.start();
            }
        }
    }

    public static void sendBroadcastMessage(Message message) {
        connectionMap.values()
                .forEach(connection -> sendMessage(connection, message));
    }

    public static void sendMessage(Connection connection, Message message) {
        try {
            connection.send(message);
        }
        catch (SendingMessageException e) {
            throw new SendingMessageException(connection.getRemoteSocketAddress());
        }
    }

    private static class Handler extends Thread {
        private Socket socket;

        public Handler(Socket socket) {
            this.socket = socket;
        }

        @Override
        public void run() {
            String username = null;

            try (var connection = new Connection(socket)) {
                logger.info(CONNECTION_IS_ESTABLISHED, socket.getRemoteSocketAddress());
                username = serverHandshake(connection);
                sendBroadcastMessage(new Message(MessageType.USER_ADDED, username));
                notifyAboutUsers(connection, username);
                serverMainLoop(connection, username);
            }
            finally {
                if (username != null)
                    deleteUser(username);
            }
        }

        private void deleteUser(String username) {
            connectionMap.remove(username);
            sendBroadcastMessage(new Message(MessageType.USER_REMOVED, username));
            logger.info(CONNECTION_IS_CLOSED, socket.getRemoteSocketAddress());
        }

        private String serverHandshake(Connection connection) {
            Message username;
            do {
                connection.send(new Message(MessageType.NAME_REQUEST));
                username = connection.receive();
            } while (usernameIsNotValid(username));

            connectionMap.put(username.data(), connection);
            connection.send(new Message(MessageType.NAME_ACCEPTED));
            return username.data();
        }

        private void notifyAboutUsers(Connection connection, String username) {
            connectionMap.keySet()
                    .stream()
                    .filter(name -> !name.equals(username))
                    .forEach(name -> connection.send(new Message(MessageType.USER_ADDED, name)));
        }

        private void serverMainLoop(Connection connection, String username) {
            while (true) {
                var answer = connection.receive();
                Message message;

                switch (answer.type()) {
                    case GLOBAL_TEXT -> {
                        message = getGlobalChatMessage(username, answer.data());
                        sendBroadcastMessage(message);
                    }
                    case PRIVATE_TEXT -> {
                        var receiver = getPrivateMessageReceiver(answer.data());

                        if (username.equals(receiver))
                            message = getPrivateChatMessage(username, answer.data());
                        else if (usernameIsOnline(receiver)) {
                            message = getPrivateChatMessage(username, answer.data());
                            sendMessage(connectionMap.get(receiver), message);
                        }
                        else {
                            var messageText = String.format("User %s is offline", receiver);
                            message = new Message(MessageType.PRIVATE_TEXT, messageText);
                        }

                        sendMessage(connection, message);
                    }
                    default -> throw new MessageIsNotTextException();
                }
            }

        }


        private boolean usernameIsNotValid(Message username) {
            return username.type() != MessageType.USER_NAME ||
                    username.data() == null ||
                    username.data().isEmpty() ||
                    usernameIsOnline(username.data());
        }

        private boolean usernameIsOnline(String username) {
            return username != null && connectionMap.containsKey(username);
        }

        private Message getGlobalChatMessage(String username, String messageData) {
            var messageInText = String.format("%s: %s", username, messageData);
            return new Message(MessageType.GLOBAL_TEXT, messageInText);
        }

        private Message getPrivateChatMessage(String username, String messageData) {
            var receiver = getPrivateMessageReceiver(messageData);
            var text = getPrivateMessageText(messageData);
            var messageInText = String.format("%s -> %s: %s", username, receiver, text);

            return new Message(MessageType.PRIVATE_TEXT, messageInText);
        }

        private String getPrivateMessageReceiver(String text) {
            return text.split(": ")[0]; // user: text
        }

        private String getPrivateMessageText(String text) {
            return text.split(": ")[1]; // user: text
        }

    }
}
