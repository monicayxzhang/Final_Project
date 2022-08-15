package com.example.finalproject.ui.account;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.finalproject.R;
import com.example.finalproject.SignInActivity;
import com.example.finalproject.SignUpActivity;
import com.example.finalproject.ui.community.Post;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.squareup.picasso.Picasso;

public class AccountFragment extends Fragment implements View.OnClickListener {
    private TextView usernameTV;
    private TextView emailTV;
    private ImageView profilePicIV;
    private TextView postsCount;
    private TextView postsPercent;
    private TextView likesCount;
    private TextView likesPercent;
    int postsN;
    int likesN;
    private Button signOut;
    private ActivityResultLauncher<Intent> ImageResultLauncher;
    private DatabaseReference dbRef;
    private FirebaseUser user;
    private StorageReference storageRef;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_account, container, false);

        dbRef = FirebaseDatabase.getInstance().getReference();
        user = FirebaseAuth.getInstance().getCurrentUser();
        usernameTV = view.findViewById(R.id.account_username);
        usernameTV.setText(user.getDisplayName());
        emailTV = view.findViewById(R.id.account_email);
        emailTV.setText(user.getEmail());

        postsCount = view.findViewById(R.id.posts_count);
        postsPercent = view.findViewById(R.id.posts_percent);
        likesCount = view.findViewById(R.id.sparkles_count);
        likesPercent = view.findViewById(R.id.sparkles_percent);
        Count();

        signOut = view.findViewById(R.id.account_signout);
        signOut.setOnClickListener(this);

        profilePicIV = view.findViewById(R.id.account_profile_pic);
        profilePicIV.setOnClickListener(this);

        storageRef = FirebaseStorage.getInstance().getReference();

        storageRef.child(user.getUid()).getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
            @Override
            public void onSuccess(Uri uri) {
                Picasso.get().load(uri).into(profilePicIV);
            }
        });

        ImageResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                new ActivityResultCallback<ActivityResult>() {
                    @Override
                    public void onActivityResult(ActivityResult result) {
                        if (result.getResultCode() == Activity.RESULT_OK) {
                            // There are no request codes
                            Uri imageUri = result.getData().getData();
                            storageRef.child(user.getUid()).putFile(imageUri);
                        }
                    }
                });

        return view;
    }

    private void changeProfilePic() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        ImageResultLauncher.launch(intent);
    }

    private void signOut() {
        FirebaseAuth.getInstance().signOut();
        Intent intent = new Intent(getActivity(), SignUpActivity.class);
        startActivity(intent);
    }

    private void Count() {
        postsN = 0;
        likesN = 0;
        dbRef.child("posts")
                .orderByChild("user").equalTo(user.getUid()).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        for (DataSnapshot postSnapshot : snapshot.getChildren()) {
                            postsN += 1;
                            likesN += postSnapshot.getValue(Post.class).likes;
                        }
                        postsCount.setText(String.valueOf(postsN));
                        likesCount.setText(String.valueOf(likesN));
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.account_profile_pic:
                changeProfilePic();
                break;
            case R.id.account_signout:
                signOut();
                break;
        }
    }

}