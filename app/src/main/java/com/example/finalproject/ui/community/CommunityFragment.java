package com.example.finalproject.ui.community;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.finalproject.R;

public class CommunityFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        CommunityViewModel communityViewModel =
                new ViewModelProvider(this).get(CommunityViewModel.class);

        return inflater.inflate(R.layout.fragment_community, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}