package com.example.finalproject.ui.community;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.finalproject.R;
import com.example.finalproject.ui.community.tagcheckboxrv.TagCheckbox;
import com.example.finalproject.ui.community.tagcheckboxrv.TagCheckboxAdapter;
import com.example.finalproject.ui.community.tagrv.TagAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class EditPostActivity extends AppCompatActivity {
    private EditText subject;
    private EditText body;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;
    private List<TagCheckbox> tags = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        subject = findViewById(R.id.post_subject);
        body = findViewById(R.id.post_body);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();

        RecyclerView tagsRV = findViewById(R.id.edit_post_tags_rv);
        tagsRV.setHasFixedSize(true);
        tagsRV.setLayoutManager(new LinearLayoutManager(this));
        TagCheckboxAdapter tagAdapter = new TagCheckboxAdapter(tags);
        tagsRV.setAdapter(tagAdapter);

        mDatabase.child("tags").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    tags.add(new TagCheckbox(snapshot.getValue(String.class), false));
                    tagAdapter.notifyItemInserted(tags.size());
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

    public void cancelPost(View view) {
        finish();
    }

    public void submitPost(View view) {
        Map<String, Object> map = new HashMap();
        map.put("subject", subject.getText().toString());
        map.put("body", body.getText().toString());
        map.put("user", mAuth.getCurrentUser().getUid());
        map.put("dateTime", ServerValue.TIMESTAMP);
        map.put("likes", 0);
        map.put("postType", PostType.CONTENT.toString());
        String postID = mDatabase.child("posts").push().getKey();
        map.put("postID", postID);
        mDatabase.child("posts").child(postID).setValue(map);

        for (TagCheckbox tagCheckbox : tags) {
            if (tagCheckbox.checked) {
                mDatabase.child("postTags").child(postID).child(tagCheckbox.tag).setValue(tagCheckbox.tag);
            }
        }

        finish();
    }
}