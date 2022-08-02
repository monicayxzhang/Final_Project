package com.example.finalproject.ui.community;

import java.time.LocalDateTime;

public class Post {
    public String subject;
    public String body;
    public String user;
    public long dateTime;

    public Post() {

    }

    public Post(String subject, String body, String user, long dateTime) {
        this.subject = subject;
        this.body = body;
        this.user = user;
        this.dateTime = dateTime;
    }
}
