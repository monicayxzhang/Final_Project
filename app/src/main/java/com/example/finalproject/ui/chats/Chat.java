package com.example.finalproject.ui.chats;

public class Chat {
    public String user;
    public String message;
    public long dateTime;

    public Chat() {

    }

    public Chat(String user, String message, long dateTime) {
        this.user = user;
        this.message = message;
        this.dateTime = dateTime;
    }
}
