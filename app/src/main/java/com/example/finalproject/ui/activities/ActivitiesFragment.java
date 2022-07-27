package com.example.finalproject.ui.activities;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.example.finalproject.R;

public class ActivitiesFragment extends Fragment {

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        ActivitiesViewModel activitiesViewModel =
                new ViewModelProvider(this).get(ActivitiesViewModel.class);
        return inflater.inflate(R.layout.fragment_activities, container, false);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}