package com.example.finalproject.ui.community;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;

import com.example.finalproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ServerValue;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

public class EditPostActivity extends AppCompatActivity {
    private EditText subject;
    private EditText body;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_post);

        subject = findViewById(R.id.post_subject);
        body = findViewById(R.id.post_body);
        mDatabase = FirebaseDatabase.getInstance().getReference();
        mAuth = FirebaseAuth.getInstance();
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
        mDatabase.child("posts").push().setValue(map);
        finish();
    }
}