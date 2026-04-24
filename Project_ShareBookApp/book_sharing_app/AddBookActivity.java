package com.example.book_sharing_app;

import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.book_sharing_app.models.Book;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;

public class AddBookActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView ivBook;
    private EditText etBookName, etBookPrice, etBookDescription;
    private Uri imageUri;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_book);

        db = new DatabaseHelper(this);
        ivBook = findViewById(R.id.ivBook);
        etBookName = findViewById(R.id.etBookName);
        etBookPrice = findViewById(R.id.etBookPrice);
        etBookDescription = findViewById(R.id.etBookDescription);

        ImageView btnBack = findViewById(R.id.btnBack);
        btnBack.setOnClickListener(v -> onBackPressed());

        ivBook.setOnClickListener(v -> openFileChooser());

        findViewById(R.id.btnAddBook).setOnClickListener(v -> {
            saveBookToDatabase();
        });
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
            // Take persistable permission
            try {
                getContentResolver().takePersistableUriPermission(imageUri, Intent.FLAG_GRANT_READ_URI_PERMISSION);
            } catch (Exception e) {
                // Fallback for some providers
            }
            ivBook.setImageURI(imageUri);
            ivBook.setPadding(0, 0, 0, 0);
            findViewById(R.id.tvUploadHint).setVisibility(View.GONE);
        }
    }

    private void saveBookToDatabase() {
        String name = etBookName.getText().toString().trim();
        String priceStr = etBookPrice.getText().toString().trim();
        String description = etBookDescription.getText().toString().trim();

        if (name.isEmpty() || priceStr.isEmpty() || description.isEmpty() || imageUri == null) {
            Toast.makeText(this, "Please fill all fields and select an image", Toast.LENGTH_SHORT).show();
            return;
        }

        double price = 0;
        try {
            price = Double.parseDouble(priceStr);
        } catch (Exception e) {
            Toast.makeText(this, "Invalid price", Toast.LENGTH_SHORT).show();
            return;
        }
        
        android.content.SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String sellerId = pref.getString("userId", "unknown");
        
        // Copy image to internal storage to avoid permission issues
        String savedImagePath = copyImageToInternalStorage(imageUri);
        if (savedImagePath == null) {
            Toast.makeText(this, "Failed to process image", Toast.LENGTH_SHORT).show();
            return;
        }

        Book book = new Book(null, name, description, price, sellerId, savedImagePath, "Available", "sale");
        
        if (db.addBook(book)) {
            Toast.makeText(this, "Book added successfully", Toast.LENGTH_SHORT).show();
            finish();
        } else {
            Toast.makeText(this, "Failed to add book", Toast.LENGTH_SHORT).show();
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
            e.printStackTrace();
            return null;
        }
    }
}
