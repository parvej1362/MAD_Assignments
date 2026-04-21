package com.example.fragments_demo;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class BottomFragment extends Fragment {

    private TextView tvCombinedName;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_bottom, container, false);
        tvCombinedName = view.findViewById(R.id.tvCombinedName);
        return view;
    }

    public void updateName(String firstName, String lastName) {
        if (tvCombinedName != null) {
            tvCombinedName.setText(firstName + " " + lastName);
        }
    }
}
