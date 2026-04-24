package com.example.event_handling_case_study;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    // UI Components
    private TextView txtStatus;
    private Button btnStart, btnStop, btnPause;
    private ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Initialize UI components
        txtStatus = findViewById(R.id.txtStatus);
        btnStart = findViewById(R.id.btnStart);
        btnStop = findViewById(R.id.btnStop);
        btnPause = findViewById(R.id.btnPause);
        progressBar = findViewById(R.id.progressBar);

        // 1. CLICK EVENTS
        
        // Start Workout Click
        btnStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtStatus.setText("Workout Started 💪");
                progressBar.setProgress(20); // Visual progress update
                Toast.makeText(MainActivity.this, "Workout Started", Toast.LENGTH_SHORT).show();
            }
        });

        // Pause Workout Click
        btnPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtStatus.setText("Workout Paused ⏸️");
                Toast.makeText(MainActivity.this, "Workout Paused", Toast.LENGTH_SHORT).show();
            }
        });

        // Stop Workout Click
        btnStop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                txtStatus.setText("Workout Stopped 🛑");
                progressBar.setProgress(0); // Reset progress on stop
                Toast.makeText(MainActivity.this, "Workout Stopped", Toast.LENGTH_SHORT).show();
            }
        });

        // 2. LONG CLICK EVENT
        
        // Long press on Start Button to Reset Workout
        btnStart.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                txtStatus.setText("Workout Reset 🔄");
                progressBar.setProgress(0);
                Toast.makeText(MainActivity.this, "Workout Reset", Toast.LENGTH_SHORT).show();
                return true; // Indicates the event was handled
            }
        });

        // 3. TOUCH EVENT
        
        // Apply touch listener on TextView
        txtStatus.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    Toast.makeText(MainActivity.this, "Screen Touched", Toast.LENGTH_SHORT).show();
                }
                return false; // Return false to allow other events (like click) if needed
            }
        });
    }

    // 4. KEY EVENT
    
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        // Override back button (Key Code 4)
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            Toast.makeText(this, "Do you want to exit?", Toast.LENGTH_SHORT).show();
            // Returning true prevents the default back action (exit)
            return true;
        }
        return super.onKeyDown(keyCode, event);
    }
}