package com.example.finalproject.ui.community.forumrv;

import android.provider.ContactsContract;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.ui.chats.Message;
import com.example.finalproject.ui.community.Post;
import com.example.finalproject.ui.community.tagrv.TagAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

public class ContentViewHolder extends ForumViewHolder {
    private TextView userTV;
    private TextView dateTimeTV;
    private TextView subjectTV;
    private TextView bodyTV;
    private TextView likesTV;
    private RecyclerView recyclerView;
    private DatabaseReference dbRef;

    public ContentViewHolder(@NonNull View itemView) {
        super(itemView);

        userTV = itemView.findViewById(R.id.post_content_user);
        dateTimeTV = itemView.findViewById(R.id.post_content_dateTime);
        subjectTV = itemView.findViewById(R.id.post_content_subject);
        bodyTV = itemView.findViewById(R.id.post_content_body);
        likesTV = itemView.findViewById(R.id.post_content_likes);

        recyclerView = itemView.findViewById(R.id.post_content_tags_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(itemView.getContext(),
                LinearLayoutManager.HORIZONTAL, false));

        dbRef = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    void bind(Post post, ForumViewHolder.OnItemClickListener listener) {
        dbRef.child("users").child(post.user).child("name").addValueEventListener(new ValueEventListener() {
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
        subjectTV.setText(post.subject);
        bodyTV.setText(post.body);
        likesTV.setText(String.valueOf(post.likes));
        likesTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dbRef.child("posts").child(post.postID).child("likes").runTransaction(new Transaction.Handler() {
                            @NonNull
                            @Override
                            public Transaction.Result doTransaction(@NonNull MutableData currentData) {
                                Integer likes = currentData.getValue(Integer.class);
                                currentData.setValue(likes + 1);
                                return Transaction.success(currentData);
                            }

                            @Override
                            public void onComplete(@Nullable DatabaseError error, boolean committed, @Nullable DataSnapshot currentData) {

                            }
                        });
            }
        });

        List<String> tags = new LinkedList<>();
        TagAdapter adapter = new TagAdapter(tags);
        recyclerView.setAdapter(adapter);

//        dbRef.child("tags").child(post.postID).addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(@NonNull DataSnapshot snapshot) {
//                for (DataSnapshot tagSnapshot : snapshot.getChildren()) {
//                    System.out.println(tagSnapshot.getValue(String.class));
//                    tags.add(tagSnapshot.getValue(String.class));
//                }
//                adapter.notifyItemRangeInserted(0, tags.size());
//            }
//
//            @Override
//            public void onCancelled(@NonNull DatabaseError error) {
//
//            }
//        });

        dbRef.child("postTags").child(post.postID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    System.out.println(snapshot.getValue(String.class));
                    tags.add(snapshot.getValue(String.class));
                    adapter.notifyItemInserted(tags.size() - 1);
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onChildRemoved(@NonNull DataSnapshot snapshot) {

            }

            @Override
            public void onChildMoved(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        itemView.setOnLongClickListener(v -> listener.onLongClick(post, v));
    }
}
