package com.example.finalproject.ui.chats.chatrv;

import android.net.Uri;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.ui.chats.Message;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChatViewHolder extends RecyclerView.ViewHolder {
    public interface OnItemClickListener {
        void onItemClick(Message chat);
    }

    private TextView userTV;
    private TextView messageTV;
    private TextView dateTimeTV;
    private ImageView profilePicIV;

    public ChatViewHolder(@NonNull View itemView) {
        super(itemView);

        userTV = itemView.findViewById(R.id.chat_user);
        messageTV = itemView.findViewById(R.id.chat_message);
        dateTimeTV = itemView.findViewById(R.id.chat_time);
        profilePicIV = itemView.findViewById(R.id.chat_profile_pic);
    }

    public void bind(Message chat, OnItemClickListener listener) {
        Query query = FirebaseDatabase.getInstance().getReference()
                .child("users").orderByKey().equalTo(chat.recipient);
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

        messageTV.setText(chat.text);
        SimpleDateFormat formatter = new SimpleDateFormat("MM-dd HH:mm");
        dateTimeTV.setText(formatter.format(new Date(chat.dateTime)));
        FirebaseStorage.getInstance().getReference()
                .child(chat.recipient).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profilePicIV);
            }
        });

        itemView.setOnClickListener(v -> listener.onItemClick(chat));
    }
}
