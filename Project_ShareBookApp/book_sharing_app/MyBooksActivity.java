package com.example.book_sharing_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_sharing_app.models.Book;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.List;

public class MyBooksActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private MyBookAdapter adapter;
    private DatabaseHelper db;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_books);

        db = new DatabaseHelper(this);
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        currentUserId = pref.getString("userId", "");

        recyclerView = findViewById(R.id.recyclerViewMyBooks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> onBackPressed());

        FloatingActionButton fabAddBook = findViewById(R.id.fabAddBook);
        fabAddBook.setOnClickListener(v -> startActivity(new Intent(MyBooksActivity.this, AddBookActivity.class)));

        loadMyBooks();
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentUserId == null || currentUserId.isEmpty()) {
            Toast.makeText(this, "Session expired. Please login again.", Toast.LENGTH_SHORT).show();
            startActivity(new Intent(this, RoleSelectionActivity.class));
            finish();
            return;
        }

        loadMyBooks();
    }

    private void loadMyBooks() {
        List<Book> bookList = db.getBooksBySeller(currentUserId);
        adapter = new MyBookAdapter(this, bookList, new MyBookAdapter.OnActionClickListener() {
            @Override
            public void onEdit(Book book) {
                Intent intent = new Intent(MyBooksActivity.this, EditBookActivity.class);
                intent.putExtra("bookId", book.getId());
                startActivity(intent);
            }

            @Override
            public void onDelete(Book book) {
                new AlertDialog.Builder(MyBooksActivity.this)
                        .setTitle("Delete Book")
                        .setMessage("Are you sure you want to delete " + book.getTitle() + "?")
                        .setPositiveButton("Yes", (dialog, which) -> {
                            if (db.deleteBook(book.getId())) {
                                Toast.makeText(MyBooksActivity.this, "Deleted", Toast.LENGTH_SHORT).show();
                                loadMyBooks();
                            } else {
                                Toast.makeText(MyBooksActivity.this, "Failed to delete", Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("No", null)
                        .show();
            }
        });
        recyclerView.setAdapter(adapter);
    }
}
