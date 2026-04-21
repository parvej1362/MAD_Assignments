package com.example.input_controls;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class ResultActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_result);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(android.R.id.content), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView tvResultData = findViewById(R.id.tvResultData);
        Button btnBack = findViewById(R.id.btnBack);

        // Get data from Intent
        String name = getIntent().getStringExtra("NAME");
        String email = getIntent().getStringExtra("EMAIL");
        String gender = getIntent().getStringExtra("GENDER");
        String country = getIntent().getStringExtra("COUNTRY");
        boolean notifications = getIntent().getBooleanExtra("NOTIFICATIONS", false);
        float rating = getIntent().getFloatExtra("RATING", 0.0f);

        StringBuilder resultBuilder = new StringBuilder();
        resultBuilder.append("Name: ").append(name).append("\n")
                .append("Email: ").append(email).append("\n")
                .append("Gender: ").append(gender).append("\n")
                .append("Country: ").append(country).append("\n")
                .append("Notifications: ").append(notifications ? "Yes" : "No").append("\n")
                .append("Rating: ").append(rating).append(" Stars");

        tvResultData.setText(resultBuilder.toString());

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish(); // Close activity
            }
        });
    }
}
