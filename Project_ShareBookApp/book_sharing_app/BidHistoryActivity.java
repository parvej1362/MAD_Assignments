package com.example.book_sharing_app;

import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_sharing_app.models.Auction;
import com.example.book_sharing_app.models.Bid;

import java.util.List;

public class BidHistoryActivity extends AppCompatActivity {

    private RecyclerView recyclerViewBids;
    private BidAdapter adapter;
    private DatabaseHelper db;
    private String auctionId;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bid_history);

        db = new DatabaseHelper(this);
        auctionId = getIntent().getStringExtra("auctionId");
        recyclerViewBids = findViewById(R.id.recyclerViewBids);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> onBackPressed());

        recyclerViewBids.setLayoutManager(new LinearLayoutManager(this));
        loadBids();
    }

    private void loadBids() {
        List<Bid> bidList = db.getBidsForAuction(auctionId);
        
        Auction auction = null;
        for (Auction a : db.getAllAuctions()) {
            if (a.getId().equals(auctionId)) {
                auction = a;
                break;
            }
        }
        
        boolean isClosed = (auction != null && "CLOSED".equals(auction.getStatus()));
        adapter = new BidAdapter(this, bidList, isClosed);
        recyclerViewBids.setAdapter(adapter);
    }
}
