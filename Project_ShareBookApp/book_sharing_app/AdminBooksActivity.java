package com.example.book_sharing_app;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_sharing_app.models.Book;

import java.util.List;

public class AdminBooksActivity extends AppCompatActivity {

    private RecyclerView recyclerView;
    private BookAdapter adapter;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_books);

        db = new DatabaseHelper(this);
        recyclerView = findViewById(R.id.recyclerViewBooks);
        recyclerView.setLayoutManager(new LinearLayoutManager(this));

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> onBackPressed());

        loadBooks();
    }

    private void loadBooks() {
        List<Book> bookList = db.getAllBooks();
        adapter = new BookAdapter(this, bookList, new BookAdapter.OnBookClickListener() {
            @Override
            public void onBookClick(Book book) {
                // View details if needed
            }

            @Override
            public void onEditClick(Book book) { }

            @Override
            public void onDeleteClick(Book book) { }
        });
        recyclerView.setAdapter(adapter);
    }
}
