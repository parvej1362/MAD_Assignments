package com.example.registration_firebase_connection;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends AppCompatActivity {

    private EditText edtName, edtEmail, edtPassword, edtAddress;
    private Button btnRegister, btnShowUsers;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize Firebase
        databaseRef = FirebaseDatabase.getInstance().getReference("users");

        // Initialize UI Elements
        edtName = findViewById(R.id.edtName);
        edtEmail = findViewById(R.id.edtEmail);
        edtPassword = findViewById(R.id.edtPassword);
        edtAddress = findViewById(R.id.edtAddress);
        btnRegister = findViewById(R.id.btnRegister);
        btnShowUsers = findViewById(R.id.btnShowUsers);

        // Register Button Click Listener
        btnRegister.setOnClickListener(v -> registerUser());

        // Show Users Button Click Listener
        btnShowUsers.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, UsersActivity.class);
            startActivity(intent);
        });
    }

    private void registerUser() {
        String name = edtName.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String address = edtAddress.getText().toString().trim();

        if (TextUtils.isEmpty(name) || TextUtils.isEmpty(email) || 
            TextUtils.isEmpty(password) || TextUtils.isEmpty(address)) {
            Toast.makeText(MainActivity.this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        // Show Progress Dialog
        android.app.ProgressDialog progressDialog = new android.app.ProgressDialog(this);
        progressDialog.setMessage("Registering User...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // Generate unique key for each user
        String id = databaseRef.push().getKey();
        User user = new User(id, name, email, password, address);

        if (id != null) {
            databaseRef.child(id).setValue(user)
                .addOnSuccessListener(aVoid -> {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "User Registered Successfully", Toast.LENGTH_SHORT).show();
                    clearFields();
                })
                .addOnFailureListener(e -> {
                    progressDialog.dismiss();
                    Toast.makeText(MainActivity.this, "Registration Failed: " + e.getMessage(), Toast.LENGTH_SHORT).show();
                });
        }
    }

    private void clearFields() {
        edtName.setText("");
        edtEmail.setText("");
        edtPassword.setText("");
        edtAddress.setText("");
    }
}