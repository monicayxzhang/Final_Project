package com.example.finalproject.ui.community.postrv;

import static java.text.DateFormat.getDateTimeInstance;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.ui.community.Post;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.time.format.DateTimeFormatter;
import java.util.Date;

public class PostViewHolder extends RecyclerView.ViewHolder {
    public interface OnItemClickListener {
        void onItemClick(Post post);
    }

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

    public void bind(Post post, OnItemClickListener listener) {
        Query query = FirebaseDatabase.getInstance().getReference()
                .child("users").orderByKey().equalTo(post.user);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                    userTV.setText(userSnapshot.child("name").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
        subjectTV.setText(post.subject);
        bodyTV.setText(post.body);
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
        //DateTimeFormatter formatter = DateTimeFormatter.ofPattern("MM-dd HH:mm");
        dateTimeTV.setText(formatter.format(new Date(post.dateTime)));

        itemView.setOnClickListener(v -> listener.onItemClick(post));
    }
}
