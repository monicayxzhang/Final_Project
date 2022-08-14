package com.example.finalproject.ui.community.tagrv;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;

import java.util.List;

public class TagAdapter extends RecyclerView.Adapter<TagViewHolder> {
    private List<String> tags;

    public TagAdapter(List<String> tags) {
        this.tags = tags;
    }

    @NonNull
    @Override
    public TagViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TagViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_post_content_tag, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TagViewHolder holder, int position) {
        holder.bind(tags.get(position));
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }
}
