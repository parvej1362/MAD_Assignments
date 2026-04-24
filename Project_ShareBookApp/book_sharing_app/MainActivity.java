package com.example.book_sharing_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_sharing_app.models.Book;
import com.google.android.material.chip.ChipGroup;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private RecyclerView recyclerViewBooks;
    private BookAdapter adapter;
    private DatabaseHelper db;
    private TextView tvCartBadge;
    private EditText etSearch;
    private ChipGroup chipGroupPrice;
    
    private List<Book> allBooks = new ArrayList<>();
    private String currentQuery = "";
    private double minPrice = -1;
    private double maxPrice = -1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        db = new DatabaseHelper(this);
        drawerLayout = findViewById(R.id.drawer_layout);
        recyclerViewBooks = findViewById(R.id.recyclerViewBooks);
        tvCartBadge = findViewById(R.id.tvCartBadge);
        etSearch = findViewById(R.id.etSearch);
        chipGroupPrice = findViewById(R.id.chipGroupPrice);
        
        ImageView btnMenu = findViewById(R.id.btnMenu);
        RelativeLayout btnCart = findViewById(R.id.btnCart);
        FloatingActionButton fabAddBook = findViewById(R.id.fabAddBook);
        FloatingActionButton fabAuction = findViewById(R.id.fabAuction);

        btnMenu.setOnClickListener(v -> drawerLayout.openDrawer(GravityCompat.START));
        btnCart.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, CartActivity.class)));
        fabAddBook.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddBookActivity.class)));
        fabAuction.setOnClickListener(v -> startActivity(new Intent(MainActivity.this, AddAuctionActivity.class)));

        recyclerViewBooks.setLayoutManager(new LinearLayoutManager(this));
        
        // Search logic
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                currentQuery = s.toString().toLowerCase();
                applyFilters();
            }
            @Override
            public void afterTextChanged(Editable s) {}
        });

        // Price Filter logic
        chipGroupPrice.setOnCheckedChangeListener((group, checkedId) -> {
            if (checkedId == R.id.chip0_100) {
                minPrice = 0; maxPrice = 100;
            } else if (checkedId == R.id.chip100_250) {
                minPrice = 100; maxPrice = 250;
            } else if (checkedId == R.id.chip250_500) {
                minPrice = 250; maxPrice = 500;
            } else if (checkedId == R.id.chip500_1000) {
                minPrice = 500; maxPrice = 1000;
            } else if (checkedId == R.id.chip1000_plus) {
                minPrice = 1000; maxPrice = 1000000;
            } else {
                minPrice = -1; maxPrice = -1;
            }
            applyFilters();
        });

        updateCartBadge();
        loadBooks();

        // Sidebar logic
        findViewById(R.id.nav_my_books).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, MyBooksActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        findViewById(R.id.nav_my_auctions).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, MyAuctionsActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        findViewById(R.id.nav_search_auctions).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, AuctionListActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        findViewById(R.id.nav_profile).setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, ProfileActivity.class));
            drawerLayout.closeDrawer(GravityCompat.START);
        });

        findViewById(R.id.nav_logout).setOnClickListener(v -> {
            SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            pref.edit().clear().apply();
            startActivity(new Intent(MainActivity.this, RoleSelectionActivity.class));
            finishAffinity();
        });
    }

    private void applyFilters() {
        List<Book> filteredList = new ArrayList<>();
        for (Book book : allBooks) {
            boolean matchesQuery = book.getTitle().toLowerCase().contains(currentQuery);
            boolean matchesPrice = true;

            if (minPrice != -1) {
                double price = book.getPrice();
                matchesPrice = price >= minPrice && price <= maxPrice;
            }

            if (matchesQuery && matchesPrice) {
                filteredList.add(book);
            }
        }
        if (adapter != null) {
            adapter.updateList(filteredList);
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        updateCartBadge();
        loadBooks();
    }

    private void updateCartBadge() {
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String currentUserId = pref.getString("userId", "");
        if (!currentUserId.isEmpty()) {
            int count = db.getCartBooks(currentUserId).size();
            tvCartBadge.setText(String.valueOf(count));
        }
    }

    private void loadBooks() {
        allBooks = db.getAllBooks();
        adapter = new BookAdapter(this, allBooks, new BookAdapter.OnBookClickListener() {
            @Override
            public void onBookClick(Book book) {
                Intent intent = new Intent(MainActivity.this, UserBookDetailActivity.class);
                intent.putExtra("bookId", book.getId());
                startActivity(intent);
            }

            @Override
            public void onEditClick(Book book) { }

            @Override
            public void onDeleteClick(Book book) { }
        });
        recyclerViewBooks.setAdapter(adapter);
        applyFilters(); // Apply current filters to the newly loaded list
    }
}