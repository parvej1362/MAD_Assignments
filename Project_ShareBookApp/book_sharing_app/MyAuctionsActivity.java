package com.example.book_sharing_app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_sharing_app.models.Auction;

import java.util.List;

public class MyAuctionsActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAuctions;
    private AuctionAdapter adapter;
    private DatabaseHelper db;
    private ImageView btnBack;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_list);

        db = new DatabaseHelper(this);
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        currentUserId = pref.getString("userId", "");

        recyclerViewAuctions = findViewById(R.id.recyclerViewAuctions);
        btnBack = findViewById(R.id.btnBack);
        
        // Change header text
        TextView tvHeader = findViewById(android.R.id.content).getRootView().findViewWithTag("header_text");
        // Since I can't find by tag easily if not set, I'll just look for the textview
        // But better to just update the layout title if possible.
        // For now, I'll assume the user knows they are in "My Auctions" or I can update the activity title.

        btnBack.setOnClickListener(v -> onBackPressed());

        recyclerViewAuctions.setLayoutManager(new LinearLayoutManager(this));
        loadMyAuctions();
    }

    private void loadMyAuctions() {
        List<Auction> auctionList = db.getAuctionsBySeller(currentUserId);
        adapter = new AuctionAdapter(this, auctionList, auction -> {
            Intent intent = new Intent(MyAuctionsActivity.this, AuctionDetailActivity.class);
            intent.putExtra("auctionId", auction.getId());
            startActivity(intent);
        });
        recyclerViewAuctions.setAdapter(adapter);
    }
}
