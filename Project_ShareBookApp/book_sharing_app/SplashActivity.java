package com.example.book_sharing_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import androidx.appcompat.app.AppCompatActivity;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                String userId = pref.getString("userId", null);
                
                if (userId != null) {
                    // Check if it was admin (we'll assume users for now, or you can save role in prefs)
                    // For better logic, let's just go to RoleSelection if session is ambiguous
                    // But usually, we save the role too.
                    startActivity(new Intent(SplashActivity.this, MainActivity.class));
                } else {
                    startActivity(new Intent(SplashActivity.this, RoleSelectionActivity.class));
                }
                finish();
            }
        }, 2000);
    }
}
