package com.example.finalproject.ui.community.forumrv;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;
import com.example.finalproject.ui.chats.Message;
import com.example.finalproject.ui.chats.chatrv.ChatViewHolder;
import com.example.finalproject.ui.chats.messagerv.MessageAdapter;
import com.example.finalproject.ui.community.Post;
import com.example.finalproject.ui.community.PostType;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import org.w3c.dom.Text;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class ForumAdapter extends RecyclerView.Adapter<ForumViewHolder> {
    private static final int ITEM_CONTENT = 1;
    private static final int ITEM_COMMENT = 2;
    private static final int ITEM_SUBCOMMENT = 3;
    private List<Post> posts;
    private Context context;

    public ForumAdapter(List<Post> posts, Context context) {
        this.posts = posts;
        this.context = context;
    }

    @NonNull
    @Override
    public ForumViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        if (viewType == ITEM_CONTENT) {
            return new ContentViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_post_content, parent, false));
        } else if (viewType == ITEM_COMMENT) {
            return new CommentViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_post_comment, parent, false));
        } else {
            return new SubCommentViewHolder(LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.item_post_subcomment, parent, false));
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ForumViewHolder holder, int position) {
        holder.bind(posts.get(position), context);
    }

    @Override
    public int getItemViewType(int position) {
        if (position == 0)  {
            return ITEM_CONTENT;
        } else if (posts.get(position).postType == PostType.COMMENT) {
            return ITEM_COMMENT;
        } else {
            return ITEM_SUBCOMMENT;
        }
    }

    @Override
    public int getItemCount() {
        return posts.size();
    }
}
