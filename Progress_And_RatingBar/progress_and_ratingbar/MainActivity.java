package com.example.progress_and_ratingbar;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.RatingBar;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private ProgressBar progressBarHorizontal;
    private ProgressBar progressBarCircular;
    private TextView tvStatus;
    private Button btnUpload;
    
    private RatingBar ratingBar;
    private TextView tvRatingResult;

    private int progressStatus = 0;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        handler = new Handler(Looper.getMainLooper());

        progressBarHorizontal = findViewById(R.id.progressBarHorizontal);
        progressBarCircular = findViewById(R.id.progressBarCircular);
        tvStatus = findViewById(R.id.tvStatus);
        btnUpload = findViewById(R.id.btnUpload);
        
        ratingBar = findViewById(R.id.ratingBar);
        tvRatingResult = findViewById(R.id.tvRatingResult);

        btnUpload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startUpload();
            }
        });

        ratingBar.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                tvRatingResult.setText("Rating: " + rating);
            }
        });
    }

    private void startUpload() {
        // Reset properties
        progressStatus = 0;
        progressBarHorizontal.setProgress(0);
        progressBarCircular.setVisibility(View.VISIBLE);
        btnUpload.setEnabled(false);

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (progressStatus < 100) {
                    progressStatus += 5;

                    handler.post(new Runnable() {
                        @Override
                        public void run() {
                            progressBarHorizontal.setProgress(progressStatus);
                            tvStatus.setText("Uploading... " + progressStatus + "/" + progressBarHorizontal.getMax());
                        }
                    });

                    try {
                        Thread.sleep(200); // Simulate network delay
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

                // File upload complete
                handler.post(new Runnable() {
                    @Override
                    public void run() {
                        progressBarCircular.setVisibility(View.INVISIBLE);
                        tvStatus.setText("Upload Complete!");
                        btnUpload.setEnabled(true);
                    }
                });
            }
        }).start();
    }
}