package com.example.finalproject.ui.community;

import android.content.Intent;
import android.nfc.Tag;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.core.view.MenuProvider;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.ui.chats.MessageActivity;
import com.example.finalproject.ui.community.postrv.PostAdapter;
import com.example.finalproject.ui.community.postrv.PostViewHolder;
import com.example.finalproject.ui.community.tagcheckboxrv.TagCheckbox;
import com.example.finalproject.ui.community.tagcheckboxrv.TagCheckboxAdapter;
import com.example.finalproject.ui.community.tagrv.TagAdapter;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class CommunityFragment extends Fragment implements PostViewHolder.OnItemClickListener, View.OnClickListener {
    private List<Post> posts = new ArrayList<>();
    private List<Post> selectedPosts = new LinkedList<>();
    private List<TagCheckbox> tags = new LinkedList<>();
    private DatabaseReference mDatabase;
    private RecyclerView postRV;
    private PostAdapter postAdapter;
    private PostAdapter selectedPostAdapter;
    private TagCheckboxAdapter tagAdapter;
    private DrawerLayout drawerLayout;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_community, container, false);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar_community);

//        AppCompatActivity activity = (AppCompatActivity) getActivity();
//        activity.setSupportActionBar(toolbar);
//        activity.getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        activity.getSupportActionBar().setHomeAsUpIndicator(android.R.drawable.ic_menu_sort_by_size);
//        activity.getSupportActionBar().setTitle("");
        toolbar.inflateMenu(R.menu.toolbar_community_menu);
        toolbar.setOnMenuItemClickListener(item -> {
            switch (item.getItemId()) {
                case R.id.toolbar_community_post:
                    editPost();
                    return true;
                default:
                    return false;
            }
        });
        ImageButton searchMenu = view.findViewById(R.id.community_search_menu);
        drawerLayout = view.findViewById(R.id.community_drawer_layout);
        searchMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        Button apply = view.findViewById(R.id.community_apply);
        apply.setOnClickListener(this);
        Button clear = view.findViewById(R.id.community_clear);
        clear.setOnClickListener(this);

        RecyclerView tagsRV = view.findViewById(R.id.community_tag_rv);
        tagsRV.setHasFixedSize(true);
        tagsRV.setLayoutManager(new LinearLayoutManager(view.getContext()));
        tagAdapter = new TagCheckboxAdapter(tags);
        tagsRV.setAdapter(tagAdapter);

        mDatabase = FirebaseDatabase.getInstance().getReference();
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

        postRV = view.findViewById(R.id.rv_community);
        postRV.setHasFixedSize(true);
        postRV.setLayoutManager(new LinearLayoutManager(view.getContext()));
        postAdapter = new PostAdapter(posts, this);
        selectedPostAdapter = new PostAdapter(selectedPosts, this);
        postRV.setAdapter(postAdapter);

        postRV.addItemDecoration(new DividerItemDecoration(view.getContext(),
                DividerItemDecoration.VERTICAL));

        mDatabase.child("posts").addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot snapshot, @Nullable String previousChildName) {
                if (snapshot.exists()) {
                    posts.add(0, snapshot.getValue(Post.class));
                    postAdapter.notifyItemInserted(0);
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

        return view;
    }

    private void editPost() {
        Intent intent = new Intent(getActivity(), EditPostActivity.class);
        startActivity(intent);
    }

    private void applyTags() {
        selectedPosts.clear();
        postRV.swapAdapter(selectedPostAdapter, false);

        Set<String> selectedTags = new HashSet<>();

        for (TagCheckbox tagCheckbox : tags) {
            if (tagCheckbox.checked) {
                selectedTags.add(tagCheckbox.tag);
            }
        }

        mDatabase.child("postTags").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot postTagSnapshot : snapshot.getChildren()) {
                    for (DataSnapshot tagSnapshot : postTagSnapshot.getChildren()) {
                        if (selectedTags.contains(tagSnapshot.getValue(String.class))) {
                            mDatabase.child("posts").child(postTagSnapshot.getKey())
                                    .addListenerForSingleValueEvent(new ValueEventListener() {
                                        @Override
                                        public void onDataChange(@NonNull DataSnapshot snapshot) {
                                            selectedPosts.add(0, snapshot.getValue(Post.class));
                                            selectedPostAdapter.notifyItemInserted(0);
                                        }

                                        @Override
                                        public void onCancelled(@NonNull DatabaseError error) {

                                        }
                                    });
                            break;
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    private void clearTags() {
        for (TagCheckbox tagCheckbox : tags) {
            if (tagCheckbox.checked) {
                tagCheckbox.checked = false;
            }
        }
        tagAdapter.notifyDataSetChanged();
        postRV.swapAdapter(postAdapter, false);
        drawerLayout.closeDrawer(GravityCompat.START);
    }

    @Override
    public void onItemClick(Post post) {
        Intent intent = new Intent(getActivity(), ForumActivity.class);
        intent.putExtra("postID", post.postID);
        startActivity(intent);
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.community_apply:
                applyTags();
                break;
            case R.id.community_clear:
                clearTags();
                break;
        }
    }
}