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
    private Map<String, Post> map = new HashMap<>();
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

        //recyclerView.addItemDecoration(new DividerItemDecoration(this,
                //DividerItemDecoration.VERTICAL));

        commentET = findViewById(R.id.forum_comment_editText);

        collectComments();
    }

    @Override
    public boolean onLongClick(Post post, View view) {
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
                    if (post.postType == PostType.CONTENT) {
                        submitComment();
                    } else {
                        submitSubComment(post);
                    }
                    handled = true;

                    InputMethodManager manager = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    manager.hideSoftInputFromWindow(view.getWindowToken(), 0);
                    commentET.getText().clear();
                }
                return handled;
            }
        });

        return true;
    }

    private void submitComment() {
        Map<String, Object> map = new HashMap();
        map.put("postID", postID);
        map.put("body", commentET.getText().toString());
        map.put("user", mAuth.getCurrentUser().getUid());
        map.put("dateTime", ServerValue.TIMESTAMP);
        map.put("likes", 0);
        map.put("postType", PostType.COMMENT.toString());
        String key = dbRef.child("comments").push().getKey();
        map.put("commentID", key);
        dbRef.child("comments").child(key).setValue(map);

    }

    private void submitSubComment(Post post) {
        Map<String, Object> map = new HashMap();
        map.put("commentID", post.commentID);
        map.put("body", commentET.getText().toString());
        map.put("user", mAuth.getCurrentUser().getUid());
        map.put("dateTime", ServerValue.TIMESTAMP);
        map.put("postType", PostType.SUBCOMMENT.toString());
        dbRef.child("subcomments").push().setValue(map);
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
                if (snapshot.exists()) {
                    posts.remove(0);
                    posts.add(0, snapshot.getValue(Post.class));
                    adapter.notifyItemChanged(0);
                }
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
        dbRef.child("comments").orderByChild("postID").equalTo(postID).addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    Post post = snapshot.getValue(Post.class);
                    posts.add(post);
                    map.put(post.commentID, post);
                    collectSubComments(post.commentID);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onChildChanged(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    Post post = snapshot.getValue(Post.class);
                    int index = posts.indexOf(map.get(post.commentID));
                    posts.remove(index);
                    posts.add(index, post);
                    map.put(post.commentID, post);
                    adapter.notifyItemChanged(index);
                }
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

    private void collectSubComments(String commentID) {
        dbRef.child("subcomments").orderByChild("commentID").equalTo(commentID)
                .addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    int index;
                    Post post = snapshot.getValue(Post.class);
                    if (previousChildName != null) {
                        index = posts.indexOf(map.get(previousChildName));
                    } else {
                        index = posts.indexOf(map.get(commentID));
                    }
                    posts.add(index + 1, post);
                    map.put(snapshot.getKey(), post);
                    adapter.notifyDataSetChanged();
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
}