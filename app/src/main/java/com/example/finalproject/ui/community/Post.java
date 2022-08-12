package com.example.finalproject.ui.community;

import java.time.LocalDateTime;

public class Post {
    public String ID;
    public String subject;
    public String body;
    public String user;
    public long dateTime;
    public int likes;
    public PostType postType;

    public Post() {

    }

    public Post(String ID, String subject, String body, String user,
                long dateTime, int likes, PostType postType) {
        this.ID = ID;
        this.subject = subject;
        this.body = body;
        this.user = user;
        this.dateTime = dateTime;
        this.likes = likes;
        this.postType = postType;
    }
}
