package com.example.book_sharing_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_sharing_app.models.Book;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class CartActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private CartAdapter adapter;
    private DatabaseHelper db;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cart);

        db = new DatabaseHelper(this);
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        currentUserId = pref.getString("userId", "");

        recyclerView = findViewById(R.id.recyclerViewCart);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> onBackPressed());

        MaterialButton btnPlaceOrder = findViewById(R.id.btnPlaceOrder);
        btnPlaceOrder.setOnClickListener(v -> handlePlaceOrder());

        loadCart();
    }

    private void loadCart() {
        List<Book> bookList = db.getCartBooks(currentUserId);
        adapter = new CartAdapter(this, bookList, new CartAdapter.OnRemoveClickListener() {
            @Override
            public void onRemove(Book book) {
                if (db.removeFromCart(currentUserId, book.getId())) {
                    Toast.makeText(CartActivity.this, "Item removed", Toast.LENGTH_SHORT).show();
                    loadCart();
                }
            }
        });
        recyclerView.setAdapter(adapter);

        findViewById(R.id.btnPlaceOrder).setVisibility(bookList.isEmpty() ? View.GONE : View.VISIBLE);
    }

    private void handlePlaceOrder() {
        db.clearCart(currentUserId);
        Toast.makeText(this, "Order placed successfully!", Toast.LENGTH_LONG).show();
        finish();
    }
}
