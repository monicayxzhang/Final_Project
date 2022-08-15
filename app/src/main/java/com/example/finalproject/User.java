package com.example.finalproject;

import android.net.Uri;

public class User {
    public String name;
    public int postsCount;
    public int likesCount;

    public User() {

    }

    public User(String name, int postsCount, int likesCount) {
        this.name = name;
        this.postsCount = postsCount;
        this.likesCount = likesCount;
    }
}
