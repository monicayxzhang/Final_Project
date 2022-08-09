package com.example.finalproject.ui.chats;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.finalproject.R;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {
    private TextView recipientTV;
    private EditText messageTV;
    private String recipientID;
    private DatabaseReference mDatabase;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        messageTV = findViewById(R.id.message_editText);
        mAuth = FirebaseAuth.getInstance();
        mDatabase = FirebaseDatabase.getInstance().getReference().child("chats")
                .child(mAuth.getCurrentUser().getUid());
        recipientID = getIntent().getStringExtra("recipient");
        recipientTV = findViewById(R.id.message_recipient);
        Query query = FirebaseDatabase.getInstance().getReference()
                .child("users").orderByKey().equalTo(recipientID);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot userSnapshot: snapshot.getChildren()) {
                    recipientTV.setText(userSnapshot.child("name").getValue(String.class));
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    public void sentMessage(View view) {
        Map<String, Object> map = new HashMap();
        map.put("recipient", recipientID);
        map.put("message", messageTV.getText().toString());
        map.put("dateTime", ServerValue.TIMESTAMP);
        mDatabase.push().setValue(map);
    }

    public void goBack(View view) {
        finish();
    }
}