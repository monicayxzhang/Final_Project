package com.example.finalproject.ui.chats;

public class Chat {
    public String recipient;
    public String message;
    public long dateTime;

    public Chat() {

    }

    public Chat(String recipient, String message, long dateTime) {
        this.recipient = recipient;
        this.message = message;
        this.dateTime = dateTime;
    }
}
