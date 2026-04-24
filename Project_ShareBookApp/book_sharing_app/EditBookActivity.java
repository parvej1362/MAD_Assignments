package com.example.book_sharing_app;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.book_sharing_app.models.Book;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class EditBookActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView ivBook;
    private TextInputEditText etBookName, etBookPrice, etBookDescription;
    private Uri imageUri;
    private DatabaseHelper db;
    private String bookId;
    private String currentImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_book);

        db = new DatabaseHelper(this);
        ivBook = findViewById(R.id.ivBook);
        etBookName = findViewById(R.id.etBookName);
        etBookPrice = findViewById(R.id.etBookPrice);
        etBookDescription = findViewById(R.id.etBookDescription);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> onBackPressed());

        bookId = getIntent().getStringExtra("bookId");
        if (bookId != null) {
            loadBookData();
        } else {
            Toast.makeText(this, "Book not found", Toast.LENGTH_SHORT).show();
            finish();
        }

        ivBook.setOnClickListener(v -> openFileChooser());
        findViewById(R.id.btnUpdateBook).setOnClickListener(v -> updateBookInDatabase());
    }

    private void loadBookData() {
        Book book = db.getBookById(bookId);
        if (book != null) {
            etBookName.setText(book.getTitle());
            etBookPrice.setText(String.valueOf(book.getPrice()));
            etBookDescription.setText(book.getDescription());
            currentImagePath = book.getImageUrl();

            try {
                if (currentImagePath != null && !currentImagePath.isEmpty()) {
                    File file = new File(currentImagePath);
                    if (file.exists()) {
                        ivBook.setImageURI(Uri.fromFile(file));
                        ivBook.setPadding(0, 0, 0, 0);
                        findViewById(R.id.tvUploadHint).setVisibility(View.GONE);
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent(Intent.ACTION_OPEN_DOCUMENT);
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        intent.setType("image/*");
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            imageUri = data.getData();
            ivBook.setImageURI(imageUri);
            ivBook.setPadding(0, 0, 0, 0);
            findViewById(R.id.tvUploadHint).setVisibility(View.GONE);
        }
    }

    private void updateBookInDatabase() {
        String name = etBookName.getText().toString().trim();
        String priceStr = etBookPrice.getText().toString().trim();
        String description = etBookDescription.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty() || description.isEmpty()) {
            Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = Double.parseDouble(priceStr);
        String finalImagePath = currentImagePath;

        if (imageUri != null) {
            finalImagePath = copyImageToInternalStorage(imageUri);
        }

        Book updatedBook = new Book(bookId, name, description, price, null, finalImagePath, "Available", "sale");
        
        if (db.updateBook(updatedBook)) {
            Toast.makeText(this, "Book updated successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Update failed", Toast.LENGTH_SHORT).show();
        }
    }

    private String copyImageToInternalStorage(Uri uri) {
        try {
            String fileName = "book_" + System.currentTimeMillis() + ".jpg";
            File file = new File(getFilesDir(), fileName);
            InputStream is = getContentResolver().openInputStream(uri);
            OutputStream os = new FileOutputStream(file);
            byte[] buffer = new byte[1024];
            int length;
            while ((length = is.read(buffer)) > 0) {
                os.write(buffer, 0, length);
            }
            os.flush();
            os.close();
            is.close();
            return file.getAbsolutePath();
        } catch (Exception e) {
            return null;
        }
    }
}
