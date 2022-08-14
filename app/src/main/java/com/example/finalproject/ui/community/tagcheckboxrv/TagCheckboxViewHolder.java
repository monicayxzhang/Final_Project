package com.example.finalproject.ui.community.tagcheckboxrv;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;

public class TagCheckboxViewHolder extends RecyclerView.ViewHolder {
    private TextView tagTV;

    public TagCheckboxViewHolder(@NonNull View itemView) {
        super(itemView);

        tagTV = itemView.findViewById(R.id.tag_checkbox);
    }

    public void bind(String tag) {
        tagTV.setText(tag);
    }
}
