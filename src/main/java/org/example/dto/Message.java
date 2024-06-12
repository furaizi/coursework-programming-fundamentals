package org.example.dto;

import java.io.Serializable;

public record Message(MessageType type, String data, String to) implements Serializable {

    public Message(MessageType type) {
        this(type, null, null);
    }

    public Message(MessageType type, String data) {
        this(type, data, null);
    }
}
