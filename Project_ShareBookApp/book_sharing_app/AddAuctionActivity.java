package com.example.book_sharing_app;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.book_sharing_app.models.Book;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class AddAuctionActivity extends AppCompatActivity {

    private ImageView ivSelectedBook, btnBack;
    private Spinner spinnerMyBooks;
    private TextInputEditText etStartingPrice, etEndDate, etEndTime;
    private MaterialButton btnPlaceAuction;
    private DatabaseHelper db;
    private List<Book> myBooks;
    private Book selectedBook;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_auction);

        db = new DatabaseHelper(this);
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        currentUserId = pref.getString("userId", "");

        ivSelectedBook = findViewById(R.id.ivSelectedBook);
        btnBack = findViewById(R.id.btnBack);
        spinnerMyBooks = findViewById(R.id.spinnerMyBooks);
        etStartingPrice = findViewById(R.id.etStartingPrice);
        etEndDate = findViewById(R.id.etEndDate);
        etEndTime = findViewById(R.id.etEndTime);
        btnPlaceAuction = findViewById(R.id.btnPlaceAuction);

        btnBack.setOnClickListener(v -> onBackPressed());

        loadMyBooks();

        etEndDate.setOnClickListener(v -> showDatePicker());
        etEndTime.setOnClickListener(v -> showTimePicker());

        btnPlaceAuction.setOnClickListener(v -> {
            if (selectedBook == null) {
                Toast.makeText(this, "Please select a book", Toast.LENGTH_SHORT).show();
                return;
            }
            String priceStr = etStartingPrice.getText().toString().trim();
            String dateStr = etEndDate.getText().toString().trim();
            String timeStrDisplay = etEndTime.getText().toString().trim();
            String timeStr24 = (etEndTime.getTag() != null) ? etEndTime.getTag().toString() : "";

            if (priceStr.isEmpty() || dateStr.isEmpty() || timeStrDisplay.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
                return;
            }

            double price = Double.parseDouble(priceStr);
            String endDateTime = dateStr + " " + timeStr24;

            if (db.createAuction(selectedBook.getId(), currentUserId, price, endDateTime)) {
                Toast.makeText(this, "Auction Created Successfully!", Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Toast.makeText(this, "Failed to create auction", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadMyBooks() {
        myBooks = db.getBooksBySeller(currentUserId);
        List<String> bookTitles = new ArrayList<>();
        for (Book b : myBooks) {
            bookTitles.add(b.getTitle());
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, bookTitles);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerMyBooks.setAdapter(adapter);

        spinnerMyBooks.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectedBook = myBooks.get(position);
                
                // Load book image when selected
                if (selectedBook.getImageUrl() != null && !selectedBook.getImageUrl().isEmpty()) {
                    File imgFile = new File(selectedBook.getImageUrl());
                    if (imgFile.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        ivSelectedBook.setImageBitmap(bitmap);
                    } else {
                        ivSelectedBook.setImageResource(R.drawable.ic_logo);
                    }
                } else {
                    ivSelectedBook.setImageResource(R.drawable.ic_logo);
                }
            }
            @Override
            public void onNothingSelected(AdapterView<?> parent) {}
        });
    }

    private void showDatePicker() {
        Calendar c = Calendar.getInstance();
        DatePickerDialog dialog = new DatePickerDialog(this, (view, year, month, dayOfMonth) -> {
            String date = String.format("%02d/%02d/%d", dayOfMonth, (month + 1), year);
            etEndDate.setText(date);
        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
        dialog.getDatePicker().setMinDate(System.currentTimeMillis());
        dialog.show();
    }

    private void showTimePicker() {
        Calendar c = Calendar.getInstance();
        new TimePickerDialog(this, (view, hourOfDay, minute) -> {
            String amPm = (hourOfDay >= 12) ? "PM" : "AM";
            int hour12 = (hourOfDay == 0 || hourOfDay == 12) ? 12 : hourOfDay % 12;
            String timeDisplay = String.format("%02d:%02d %s", hour12, minute, amPm);
            
            // Store 24h format for DB in tag
            String timeDB = String.format("%02d:%02d", hourOfDay, minute);
            
            etEndTime.setText(timeDisplay);
            etEndTime.setTag(timeDB);
        }, c.get(Calendar.HOUR_OF_DAY), c.get(Calendar.MINUTE), false).show();
    }
}
