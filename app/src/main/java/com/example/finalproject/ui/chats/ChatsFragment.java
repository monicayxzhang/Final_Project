package com.example.finalproject.ui.chats;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.finalproject.R;
import com.example.finalproject.ui.chats.chatrv.ChatAdapter;
import com.example.finalproject.ui.chats.chatrv.ChatViewHolder;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class ChatsFragment extends Fragment implements ChatViewHolder.OnItemClickListener {
    private List<Message> chats = new LinkedList<>();
    private FirebaseAuth mAuth;
    private Map<String, Message> map = new HashMap<>();

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_chats, container, false);

        RecyclerView recyclerView = view.findViewById(R.id.rv_chats);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new LinearLayoutManager(view.getContext()));
        ChatAdapter adapter = new ChatAdapter(chats, this);
        recyclerView.setAdapter(adapter);

        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(),
                DividerItemDecoration.VERTICAL));

        mAuth = FirebaseAuth.getInstance();
        DatabaseReference mDatabase = FirebaseDatabase.getInstance().getReference().child("chats")
                .child(mAuth.getCurrentUser().getUid());
        mDatabase.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                if (snapshot.exists()) {
                    collectChats(snapshot);
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        return view;
    }

    private void collectChats(DataSnapshot snapshot) {
        List<DataSnapshot> listSnapshot = reverseChildren(snapshot.getChildren());
        Set<String> userSetCur = new HashSet<>();
        List<Message> newChats = new LinkedList<>();
        for (DataSnapshot chatSnapshot : listSnapshot) {
            String user = (String) chatSnapshot.child("recipient").getValue();
            if (!userSetCur.contains(user)) {
                if (map.containsKey(user)) {
                    newChats.add(chatSnapshot.getValue(Message.class));
                    chats.remove(map.get(user));
                } else {
                    chats.add(chatSnapshot.getValue(Message.class));
                }
                userSetCur.add(user);
            }
        }
        chats.addAll(0, newChats);
    }

    private List<DataSnapshot> reverseChildren(Iterable<DataSnapshot> children) {
        List<DataSnapshot> list = new LinkedList<>();
        for (DataSnapshot snapshot : children) {
            list.add(0, snapshot);
        }
        return list;
    }

    @Override
    public void onItemClick(Message chat) {
        Intent intent = new Intent(getActivity(), MessageActivity.class);
        intent.putExtra("recipient", chat.recipient);
        startActivity(intent);
    }
}