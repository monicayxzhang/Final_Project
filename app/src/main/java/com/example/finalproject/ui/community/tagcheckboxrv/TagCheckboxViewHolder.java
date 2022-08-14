package com.example.finalproject.ui.community.tagcheckboxrv;

import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.finalproject.R;

public class TagCheckboxViewHolder extends RecyclerView.ViewHolder {
    private CheckBox tagCB;

    public TagCheckboxViewHolder(@NonNull View itemView) {
        super(itemView);

        tagCB = itemView.findViewById(R.id.tag_checkbox);
    }

    public void bind(TagCheckbox tagCheckbox) {
        tagCB.setText(tagCheckbox.tag);
        if (tagCheckbox.checked == false) {
            tagCB.setChecked(false);
        }
        tagCB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (((CompoundButton) view).isChecked()) {
                    tagCheckbox.checked = true;
                } else {
                    tagCheckbox.checked = false;
                }
            }
        });
    }
}
