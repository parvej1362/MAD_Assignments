package com.example.book_sharing_app;

import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_sharing_app.models.User;

import java.util.List;

public class AdminUsersActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private UserAdapter adapter;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_users);

        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewUsers);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> onBackPressed());

        loadUsers();
    }

    private void loadUsers() {
        List<User> userList = db.getAllUsers();
        adapter = new UserAdapter(this, userList, new UserAdapter.OnUserUpdateListener() {
            @Override
            public void onUpdateStatus(User user, boolean isBlocked) {
                if (db.updateUserStatus(user.getId(), isBlocked)) {
                    Toast.makeText(AdminUsersActivity.this, "Status Updated", Toast.LENGTH_SHORT).show();
                    loadUsers();
                } else {
                    Toast.makeText(AdminUsersActivity.this, "Update Failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
