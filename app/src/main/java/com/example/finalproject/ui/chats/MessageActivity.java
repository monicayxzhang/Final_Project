package com.example.finalproject.ui.chats;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.example.finalproject.R;
import com.example.finalproject.ui.chats.messagerv.MessageAdapter;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ServerValue;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class MessageActivity extends AppCompatActivity {
    private TextView recipientTV;
    private EditText messageTV;
    private String recipientID;
    private DatabaseReference mDatabaseSender;
    private DatabaseReference mDatabaseRecipient;
    private FirebaseAuth mAuth;
    private RecyclerView recyclerView;
    private MessageAdapter adapter;
    private List<Message> messages = new LinkedList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message);

        messageTV = findViewById(R.id.message_editText);
        mAuth = FirebaseAuth.getInstance();
        mDatabaseSender = FirebaseDatabase.getInstance().getReference().child("chats")
                .child(mAuth.getCurrentUser().getUid());
        recipientID = getIntent().getStringExtra("recipient");
        mDatabaseRecipient = FirebaseDatabase.getInstance().getReference().child("chats").child(recipientID);
        recipientTV = findViewById(R.id.message_recipient);
        getRecipientName();

        recyclerView = findViewById(R.id.message_rv);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        linearLayoutManager.setStackFromEnd(true);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new MessageAdapter(messages);
        recyclerView.setAdapter(adapter);

        collectMessages();
    }

    public void sentMessage(View view) {
        Map<String, Object> map = new HashMap();
        map.put("recipient", recipientID);
        map.put("text", messageTV.getText().toString());
        map.put("dateTime", ServerValue.TIMESTAMP);
        map.put("status", MessageStatus.SENT.toString());
        mDatabaseSender.push().setValue(map);
        map.put("recipient", mAuth.getCurrentUser().getUid());
        map.put("status", MessageStatus.RECEIVED.toString());
        mDatabaseRecipient.push().setValue(map);
        messageTV.getText().clear();
    }

    public void goBack(View view) {
        finish();
    }

    private void getRecipientName() {
        Query queryReName = FirebaseDatabase.getInstance().getReference()
                .child("users").orderByKey().equalTo(recipientID);
        queryReName.addValueEventListener(new ValueEventListener() {
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

    private void collectMessages() {
        Query queryMessages = mDatabaseSender.orderByChild("recipient").equalTo(recipientID);
        queryMessages.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    messages.add(snapshot.getValue(Message.class));
                    recyclerView.scrollToPosition(messages.size() - 1);
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