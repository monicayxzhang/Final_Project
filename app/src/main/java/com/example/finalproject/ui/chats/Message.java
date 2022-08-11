package com.example.finalproject.ui.chats;

public class Message {
    public String recipient;
    public String text;
    public long dateTime;
    public MessageStatus status;

    public Message() {

    }

    public Message(String recipient, String text, long dateTime, MessageStatus status) {
        this.recipient = recipient;
        this.text = text;
        this.dateTime = dateTime;
        this.status = status;
    }
}
