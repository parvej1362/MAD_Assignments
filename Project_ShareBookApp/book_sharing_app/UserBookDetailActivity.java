package com.example.book_sharing_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.book_sharing_app.models.Book;
import com.google.android.material.button.MaterialButton;

import java.io.File;

public class UserBookDetailActivity extends AppCompatActivity {

    private ImageView ivBookDetail;
    private TextView tvTitle, tvPrice, tvDescription;
    private MaterialButton btnAddToCart;
    private DatabaseHelper db;
    private String bookId;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_book_detail);

        db = new DatabaseHelper(this);
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        currentUserId = pref.getString("userId", "");

        ivBookDetail = findViewById(R.id.ivBookDetail);
        tvTitle = findViewById(R.id.tvBookTitleDetail);
        tvPrice = findViewById(R.id.tvBookPriceDetail);
        tvDescription = findViewById(R.id.tvBookDescriptionDetail);
        btnAddToCart = findViewById(R.id.btnAddToCart);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> onBackPressed());

        bookId = getIntent().getStringExtra("bookId");
        if (bookId != null) {
            loadBookDetails();
        } else {
            Toast.makeText(this, "Book not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        btnAddToCart.setOnClickListener(v -> handleAddToCart());
    }

    private void loadBookDetails() {
        Book book = db.getBookById(bookId);
        if (book != null) {
            tvTitle.setText(book.getTitle());
            tvPrice.setText("₹ " + book.getPrice());
            tvDescription.setText(book.getDescription());

            try {
                if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
                    if (book.getImageUrl().startsWith("content://")) {
                        ivBookDetail.setImageURI(Uri.parse(book.getImageUrl()));
                    } else {
                        File file = new File(book.getImageUrl());
                        if (file.exists()) {
                            ivBookDetail.setImageURI(Uri.fromFile(file));
                        } else {
                            ivBookDetail.setImageResource(R.drawable.ic_logo);
                        }
                    }
                } else {
                    ivBookDetail.setImageResource(R.drawable.ic_logo);
                }
            } catch (Exception e) {
                ivBookDetail.setImageResource(R.drawable.ic_logo);
            }

            if (db.isInCart(currentUserId, bookId)) {
                btnAddToCart.setText("REMOVE FROM CART");
            }
        }
    }

    private void handleAddToCart() {
        if (db.isInCart(currentUserId, bookId)) {
            if (db.removeFromCart(currentUserId, bookId)) {
                Toast.makeText(this, "Removed from cart", Toast.LENGTH_SHORT).show();
                btnAddToCart.setText("ADD TO CART");
            }
        } else {
            if (db.addToCart(currentUserId, bookId)) {
                Toast.makeText(this, "Added to cart", Toast.LENGTH_SHORT).show();
                btnAddToCart.setText("REMOVE FROM CART");
            }
        }
    }
}
