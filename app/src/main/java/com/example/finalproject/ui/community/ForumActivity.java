package com.example.finalproject.ui.community;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;

import com.example.finalproject.R;
import com.example.finalproject.ui.chats.MessageStatus;
import com.example.finalproject.ui.community.forumrv.ForumAdapter;
import com.example.finalproject.ui.community.forumrv.ForumViewHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ForumActivity extends AppCompatActivity implements ForumViewHolder.OnItemClickListener {
    private String postID;
    private List<Post> posts = new LinkedList<>();
    private DatabaseReference dbRef;
    private FirebaseAuth mAuth;
    private EditText commentET;
    private ForumAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forum);

        postID = getIntent().getStringExtra("postID");

        dbRef = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
        getPostContent();

        RecyclerView recyclerView = findViewById(R.id.forum_rv);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));
        adapter = new ForumAdapter(posts, this);
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(this,
                DividerItemDecoration.VERTICAL));

        commentET = findViewById(R.id.forum_comment_editText);
        commentET.setVisibility(View.INVISIBLE);

        collectComments();
    }

    @Override
    public boolean onLongClick(Post post, View view) {
        commentET.setVisibility(View.VISIBLE);
        commentET.setFocusable(true);
        commentET.setFocusableInTouchMode(true);
        if (commentET.requestFocus()) {
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(commentET, InputMethodManager.SHOW_IMPLICIT);
        }

        commentET.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                boolean handled = false;
                if (i == EditorInfo.IME_ACTION_SEND) {
                    submitComment();
                    handled = true;
                }
                return handled;
            }
        });

        return true;
    }

    private void submitComment() {
        Map<String, Object> map = new HashMap();
        map.put("ID", postID);
        map.put("body", commentET.getText().toString());
        map.put("user", mAuth.getCurrentUser().getUid());
        map.put("dateTime", ServerValue.TIMESTAMP);
        map.put("likes", 0);
        map.put("postType", PostType.COMMENT.toString());
        dbRef.child("comments").push().setValue(map);
    }

    public void getPostContent() {
        dbRef.child("posts").orderByKey().equalTo(postID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    posts.add(snapshot.getValue(Post.class));
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
    }

    public void collectComments() {
        dbRef.child("comments").orderByChild("ID").equalTo(postID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                posts.add(snapshot.getValue(Post.class));
                adapter.notifyDataSetChanged();
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
    }
}