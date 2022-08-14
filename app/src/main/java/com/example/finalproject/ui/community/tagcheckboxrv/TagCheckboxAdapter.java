package com.example.finalproject.ui.community.tagcheckboxrv;

import android.view.LayoutInflater;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;

import java.util.List;

public class TagCheckboxAdapter extends RecyclerView.Adapter<TagCheckboxViewHolder> {
    private List<String> tags;

    public TagCheckboxAdapter(List<String> tags) {
        this.tags = tags;
    }

    @NonNull
    @Override
    public TagCheckboxViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new TagCheckboxViewHolder(LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_search_tag, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull TagCheckboxViewHolder holder, int position) {
        holder.bind(tags.get(position));
    }

    @Override
    public int getItemCount() {
        return tags.size();
    }
}
