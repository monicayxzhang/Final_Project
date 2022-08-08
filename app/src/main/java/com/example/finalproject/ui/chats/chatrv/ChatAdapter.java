package com.example.finalproject.ui.chats.chatrv;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.ui.chats.Chat;

import java.util.List;

public class ChatAdapter extends RecyclerView.Adapter<ChatViewHolder> {
    private List<Chat> chats;
    private Fragment fragment;

    public ChatAdapter(List<Chat> chats, Fragment fragment) {
        this.chats = chats;
        this.fragment = fragment;
    }

    @NonNull
    @Override
    public ChatViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new ChatViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chat, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull ChatViewHolder holder, int position) {
        holder.bind(chats.get(position), (ChatViewHolder.OnItemClickListener) fragment);
    }

    @Override
    public int getItemCount() {
        return chats.size();
    }
}
