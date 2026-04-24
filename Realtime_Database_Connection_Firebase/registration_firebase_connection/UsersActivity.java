package com.example.registration_firebase_connection;

import android.os.Bundle;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class UsersActivity extends AppCompatActivity {

    private TableLayout tableLayout;
    private DatabaseReference databaseRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users);

        tableLayout = findViewById(R.id.tableLayout);
        databaseRef = FirebaseDatabase.getInstance().getReference("users");

        loadUsers();
    }

    private void loadUsers() {
        databaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear existing rows except the header
                int childCount = tableLayout.getChildCount();
                if (childCount > 1) {
                    tableLayout.removeViews(1, childCount - 1);
                }

                for (DataSnapshot data : snapshot.getChildren()) {
                    User user = data.getValue(User.class);
                    if (user != null) {
                        addRowToTable(user);
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(UsersActivity.this, "Failed to load data: " + error.getMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addRowToTable(User user) {
        TableRow row = new TableRow(this);
        row.setLayoutParams(new TableRow.LayoutParams(TableRow.LayoutParams.MATCH_PARENT, TableRow.LayoutParams.WRAP_CONTENT));
        row.setPadding(8, 8, 8, 8);

        TextView tvName = createTextView(user.name);
        TextView tvEmail = createTextView(user.email);
        TextView tvPassword = createTextView(user.password);
        TextView tvAddress = createTextView(user.address);

        row.addView(tvName);
        row.addView(tvEmail);
        row.addView(tvPassword);
        row.addView(tvAddress);

        // Add Delete Button with border container
        android.widget.FrameLayout btnContainer = new android.widget.FrameLayout(this);
        btnContainer.setBackgroundResource(R.drawable.table_cell_border);
        btnContainer.setPadding(10, 10, 10, 10);

        android.widget.Button btnDelete = new android.widget.Button(this);
        btnDelete.setText("Delete");
        btnDelete.setTextSize(12);
        btnDelete.setBackgroundTintList(android.content.res.ColorStateList.valueOf(android.graphics.Color.RED));
        btnDelete.setTextColor(android.graphics.Color.WHITE);
        btnDelete.setOnClickListener(v -> deleteUser(user.id));
        
        btnContainer.addView(btnDelete);
        row.addView(btnContainer);

        tableLayout.addView(row);
    }

    private void deleteUser(String userId) {
        if (userId == null) return;
        
        databaseRef.child(userId).removeValue()
            .addOnSuccessListener(aVoid -> Toast.makeText(UsersActivity.this, "User deleted", Toast.LENGTH_SHORT).show())
            .addOnFailureListener(e -> Toast.makeText(UsersActivity.this, "Delete failed: " + e.getMessage(), Toast.LENGTH_SHORT).show());
    }

    private TextView createTextView(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setPadding(10, 10, 10, 10);
        tv.setTextColor(android.graphics.Color.BLACK);
        tv.setBackgroundResource(R.drawable.table_cell_border);
        return tv;
    }
}
