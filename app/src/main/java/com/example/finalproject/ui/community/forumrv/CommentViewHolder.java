package com.example.finalproject.ui.community.forumrv;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.example.finalproject.R;
import com.example.finalproject.ui.chats.Message;
import com.example.finalproject.ui.community.Post;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;

public class CommentViewHolder extends ForumViewHolder {
    private TextView userTV;
    private TextView dateTimeTV;
    private TextView bodyTV;
    private TextView likesTV;

    public CommentViewHolder(@NonNull View itemView) {
        super(itemView);

        userTV = itemView.findViewById(R.id.post_comment_user);
        dateTimeTV = itemView.findViewById(R.id.post_comment_dateTime);
        bodyTV = itemView.findViewById(R.id.post_comment_body);
        likesTV = itemView.findViewById(R.id.post_comment_likes);
    }

    @Override
    void bind(Post post, ForumViewHolder.OnItemClickListener listener) {
        DatabaseReference dbRef = FirebaseDatabase.getInstance().getReference()
                .child("users").child(post.user).child("name");
        dbRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                userTV.setText(snapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
        dateTimeTV.setText(formatter.format(new Date(post.dateTime)));
        bodyTV.setText(post.body);
        likesTV.setText(String.valueOf(post.likes));

        itemView.setOnLongClickListener(v -> listener.onLongClick(post, v));
    }
}
