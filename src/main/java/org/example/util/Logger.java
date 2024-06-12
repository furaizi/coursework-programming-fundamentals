package org.example.util;

public class Logger {



    public void log(Throwable e) {
        if (e.getMessage() == null)
            ConsoleHelper.writeMessage(e.toString());
        else
            ConsoleHelper.writeMessage(e.getMessage());
    }

    public void info(String message) {
        ConsoleHelper.writeMessage(message);
    }
    public void info(String message, Object... args) {
        ConsoleHelper.writeMessage(message, args);
    }
}
