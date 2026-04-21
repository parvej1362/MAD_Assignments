package com.example.input_controls;

import android.animation.ObjectAnimator;
import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RatingBar;
import android.widget.Spinner;
import android.widget.ToggleButton;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText editName, editEmail;
    private RadioGroup radioGroupGender;
    private Spinner spinnerCountry;
    private ToggleButton toggleNotifications;
    private CheckBox checkboxTerms;
    private RatingBar ratingApp;
    private Button btnSubmit;
    private ProgressBar progressBar;

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

        // Initialize views
        editName = findViewById(R.id.editName);
        editEmail = findViewById(R.id.editEmail);
        radioGroupGender = findViewById(R.id.radioGroupGender);
        spinnerCountry = findViewById(R.id.spinnerCountry);
        toggleNotifications = findViewById(R.id.toggleNotifications);
        checkboxTerms = findViewById(R.id.checkboxTerms);
        ratingApp = findViewById(R.id.ratingApp);
        btnSubmit = findViewById(R.id.btnSubmit);
        progressBar = findViewById(R.id.progressBar);

        // Setup Spinner
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.countries_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerCountry.setAdapter(adapter);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!checkboxTerms.isChecked()) {
                    Toast.makeText(MainActivity.this, "Please agree to terms and conditions", Toast.LENGTH_SHORT).show();
                    return;
                }

                // Show ProgressBar and disable button during 'loading'
                progressBar.setVisibility(View.VISIBLE);
                btnSubmit.setEnabled(false);

                // Gather data
                String name = editName.getText().toString();
                String email = editEmail.getText().toString();
                
                int selectedGenderId = radioGroupGender.getCheckedRadioButtonId();
                String gender = "Not Specified";
                if (selectedGenderId != -1) {
                    RadioButton selectedGenderInfo = findViewById(selectedGenderId);
                    gender = selectedGenderInfo.getText().toString();
                }

                String country = spinnerCountry.getSelectedItem().toString();
                boolean wantsNotifications = toggleNotifications.isChecked();
                float rating = ratingApp.getRating();

                // Declare final variables for use inside inner class
                final String fName = name;
                final String fEmail = email;
                final String fGender = gender;
                final String fCountry = country;
                final boolean fWantsNotifications = wantsNotifications;
                final float fRating = rating;

                // Animate progress bar accurately from 0 to 100
                ObjectAnimator animation = ObjectAnimator.ofInt(progressBar, "progress", 0, 100);
                animation.setDuration(1500); // 1.5 seconds loading simulation
                animation.setInterpolator(new LinearInterpolator());
                animation.addListener(new AnimatorListenerAdapter() {
                    @Override
                    public void onAnimationEnd(Animator animation) {
                        super.onAnimationEnd(animation);
                        progressBar.setVisibility(View.GONE);
                        btnSubmit.setEnabled(true);
                        
                        // Use Explicit Intent to launch ResultActivity and pass data
                        Intent explicitIntent = new Intent(MainActivity.this, ResultActivity.class);
                        explicitIntent.putExtra("NAME", fName.isEmpty() ? "N/A" : fName);
                        explicitIntent.putExtra("EMAIL", fEmail.isEmpty() ? "N/A" : fEmail);
                        explicitIntent.putExtra("GENDER", fGender);
                        explicitIntent.putExtra("COUNTRY", fCountry);
                        explicitIntent.putExtra("NOTIFICATIONS", fWantsNotifications);
                        explicitIntent.putExtra("RATING", fRating);
                        
                        startActivity(explicitIntent);
                    }
                });
                animation.start();
            }
        });
    }
}