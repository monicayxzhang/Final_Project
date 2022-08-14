package com.example.finalproject.ui.community.tagrv;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;

public class TagViewHolder extends RecyclerView.ViewHolder {
    private TextView tagTV;

    public TagViewHolder(@NonNull View itemView) {
        super(itemView);

        tagTV = itemView.findViewById(R.id.post_content_tag);
    }

    public void bind(String tag) {
        tagTV.setText(tag);
    }
}
