package clients.bots.crocodile;

import clients.console.ConsoleClient;

import java.text.SimpleDateFormat;
import java.util.GregorianCalendar;

public class CrocodileBotClient extends ConsoleClient {


    public static void main(String[] args) {
        ConsoleClient client = new CrocodileBotClient();
        client.run();
    }

    @Override
    protected SocketThread getSocketThread() {
        return new BotSocketThread();
    }

    @Override
    protected boolean shouldSendTextFromConsole() {
        return false;
    }

    @Override
    protected String getUserName() {
        return "crocodile_bot";
    }



    public class BotSocketThread extends ConsoleClient.SocketThread {

        @Override
        protected void clientMainLoop() {
            sendTextMessage("""
                    Привіт чатіку. Я бот.
                    """);
            super.clientMainLoop();
        }

        @Override
        protected void processIncomingMessage(String message) {
            super.processIncomingMessage(message);

            String username, text;
            SimpleDateFormat formatter = null;
            try {
                username = message.split(": ")[0];
                text = message.split(": ")[1];
            } catch (Exception e) {
                return;
            }
            switch (text == null ? "" : text) {
                case "дата" -> formatter = new SimpleDateFormat("d.MM.yyyy");
                case "день" -> formatter = new SimpleDateFormat("d");
                case "місяць" -> formatter = new SimpleDateFormat("MMMM");
                case "рік" -> formatter = new SimpleDateFormat("yyyy");
                case "час" -> formatter = new SimpleDateFormat("H:mm:ss");
                case "година" -> formatter = new SimpleDateFormat("H");
                case "хвилини" -> formatter = new SimpleDateFormat("m");
                case "секунди" -> formatter = new SimpleDateFormat("s");
            }
            if (formatter != null)
                sendTextMessage("Информация для " + username + ": " +
                        formatter.format(new GregorianCalendar().getTime()));
        }
    }


}
