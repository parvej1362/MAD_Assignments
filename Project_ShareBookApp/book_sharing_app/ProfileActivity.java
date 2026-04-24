package com.example.book_sharing_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.book_sharing_app.models.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class ProfileActivity extends AppCompatActivity {

    private TextView tvName, tvEmail, tvPhone, tvAddress;
    private TextInputEditText etNewPassword;
    private DatabaseHelper db;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        db = new DatabaseHelper(this);
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        currentUserId = pref.getString("userId", "");

        tvName = findViewById(R.id.tvProfileName);
        tvEmail = findViewById(R.id.tvProfileEmail);
        tvPhone = findViewById(R.id.tvProfilePhone);
        tvAddress = findViewById(R.id.tvProfileAddress);
        etNewPassword = findViewById(R.id.etNewPassword);
        MaterialButton btnUpdatePassword = findViewById(R.id.btnUpdatePassword);
        MaterialButton btnLogout = findViewById(R.id.btnLogout);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> onBackPressed());

        btnLogout.setOnClickListener(v -> {
            SharedPreferences.Editor editor = pref.edit();
            editor.clear();
            editor.apply();
            startActivity(new Intent(ProfileActivity.this, RoleSelectionActivity.class));
            finishAffinity();
        });

        btnUpdatePassword.setOnClickListener(v -> {
            String newPassword = etNewPassword.getText().toString().trim();
            if (newPassword.isEmpty()) {
                Toast.makeText(this, "Please enter a new password", Toast.LENGTH_SHORT).show();
                return;
            }
            if (db.updateUserPassword(currentUserId, newPassword)) {
                Toast.makeText(this, "Password updated successfully", Toast.LENGTH_SHORT).show();
                etNewPassword.setText("");
            } else {
                Toast.makeText(this, "Failed to update password", Toast.LENGTH_SHORT).show();
            }
        });

        loadUserProfile();
    }

    private void loadUserProfile() {
        User user = db.getUserById(currentUserId);
        if (user != null) {
            tvName.setText(user.getName());
            tvEmail.setText(user.getEmail());
            tvPhone.setText("Phone: " + user.getMobile());
            tvAddress.setText("Address/City: " + user.getAddress());
        }
    }
}
