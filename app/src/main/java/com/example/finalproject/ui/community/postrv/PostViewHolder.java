package com.example.finalproject.ui.community.postrv;

import static java.text.DateFormat.getDateTimeInstance;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.ui.community.Post;

import java.time.format.DateTimeFormatter;
import java.util.Date;

public class PostViewHolder extends RecyclerView.ViewHolder {
    private TextView userTV;
    private TextView subjectTV;
    private TextView bodyTV;
    private TextView dateTimeTV;

    public PostViewHolder(@NonNull View itemView) {
        super(itemView);
        userTV = itemView.findViewById(R.id.item_post_user);
        subjectTV = itemView.findViewById(R.id.item_post_subject);
        bodyTV = itemView.findViewById(R.id.item_post_body);
        dateTimeTV = itemView.findViewById(R.id.item_post_dateTime);
    }

    public void bind(Post post) {
        userTV.setText(post.user);
        subjectTV.setText(post.subject);
        bodyTV.setText(post.body);
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm");
        dateTimeTV.setText(getDateTimeInstance().format(new Date(post.dateTime)));
    }
}
