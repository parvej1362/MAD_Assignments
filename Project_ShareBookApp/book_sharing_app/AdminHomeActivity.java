package com.example.book_sharing_app;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class AdminHomeActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_home);

        drawerLayout = findViewById(R.id.drawer_layout);
        ImageView btnMenu = findViewById(R.id.btnMenu);
        LinearLayout navViewUsers = findViewById(R.id.nav_view_users);
        LinearLayout navViewBooks = findViewById(R.id.nav_view_books);
        LinearLayout navLogout = findViewById(R.id.nav_logout);

        btnMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                drawerLayout.openDrawer(GravityCompat.START);
            }
        });

        navViewUsers.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHomeActivity.this, AdminUsersActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        navViewBooks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHomeActivity.this, AdminBooksActivity.class));
                drawerLayout.closeDrawer(GravityCompat.START);
            }
        });

        navLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(AdminHomeActivity.this, RoleSelectionActivity.class));
                finishAffinity();
            }
        });
    }
}
