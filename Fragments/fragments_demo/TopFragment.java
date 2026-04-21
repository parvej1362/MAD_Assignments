package com.example.fragments_demo;

import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class TopFragment extends Fragment {

    private EditText etFirstName;
    private EditText etLastName;
    private Button btnApply;

    private OnApplyClickListener listener;

    public interface OnApplyClickListener {
        void onApplyClicked(String firstName, String lastName);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        if (context instanceof OnApplyClickListener) {
            listener = (OnApplyClickListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnApplyClickListener");
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_top, container, false);
        etFirstName = view.findViewById(R.id.etFirstName);
        etLastName = view.findViewById(R.id.etLastName);
        btnApply = view.findViewById(R.id.btnApply);

        btnApply.setOnClickListener(v -> {
            String firstName = etFirstName.getText().toString();
            String lastName = etLastName.getText().toString();
            if (listener != null) {
                listener.onApplyClicked(firstName, lastName);
            }
        });

        return view;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        listener = null;
    }
}
