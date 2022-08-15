package com.example.finalproject.ui.community.forumrv;

import android.content.Context;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.ui.chats.Message;
import com.example.finalproject.ui.chats.chatrv.ChatViewHolder;
import com.example.finalproject.ui.community.Post;

public abstract class ForumViewHolder extends RecyclerView.ViewHolder {
    public interface OnItemClickListener {
        boolean onLongClick(Post post, View view);
    }

    ForumViewHolder(@NonNull View itemView) {
        super(itemView);
    }

    abstract void bind(Post post, Context context);
}
