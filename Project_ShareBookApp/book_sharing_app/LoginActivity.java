package com.example.book_sharing_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.example.book_sharing_app.models.User;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class LoginActivity extends AppCompatActivity {

    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        db = new DatabaseHelper(this);
        MaterialButton btnLogin = findViewById(R.id.btnLogin);
        TextView tvRegister = findViewById(R.id.tvRegister);
        TextInputEditText etEmail = findViewById(R.id.etEmail);
        TextInputEditText etPassword = findViewById(R.id.etPassword);

        tvRegister.setOnClickListener(v -> {
            startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
        });

        btnLogin.setOnClickListener(v -> {
            String email = etEmail.getText().toString().trim();
            String password = etPassword.getText().toString().trim();

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(LoginActivity.this, "Please enter all details", Toast.LENGTH_SHORT).show();
                return;
            }

            // Simple Admin check
            if (email.equals("admin@gmail.com") && password.equals("admin123")) {
                startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
                finish();
                return;
            }

            User user = db.loginUser(email, password);
            if (user != null) {
                if (user.isBlocked()) {
                    Toast.makeText(LoginActivity.this, "Your account is blocked by Admin", Toast.LENGTH_LONG).show();
                    return;
                }

                // Save user session
                SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                SharedPreferences.Editor editor = pref.edit();
                editor.putString("userId", user.getId());
                editor.putString("userName", user.getName());
                editor.putString("userRole", user.getRole());
                editor.apply();

                if (user.getRole().equals("admin")) {
                    startActivity(new Intent(LoginActivity.this, AdminHomeActivity.class));
                } else {
                    startActivity(new Intent(LoginActivity.this, MainActivity.class));
                }
                finish();
            } else {
                Toast.makeText(LoginActivity.this, "Invalid Email or Password", Toast.LENGTH_SHORT).show();
            }
        });
    }
}
