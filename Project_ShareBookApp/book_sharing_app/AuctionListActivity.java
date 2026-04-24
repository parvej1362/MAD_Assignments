package com.example.book_sharing_app;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_sharing_app.models.Auction;

import java.util.List;

public class AuctionListActivity extends AppCompatActivity {

    private RecyclerView recyclerViewAuctions;
    private AuctionAdapter adapter;
    private DatabaseHelper db;
    private ImageView btnBack;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_list);

        db = new DatabaseHelper(this);
        recyclerViewAuctions = findViewById(R.id.recyclerViewAuctions);
        btnBack = findViewById(R.id.btnBack);

        btnBack.setOnClickListener(v -> onBackPressed());

        recyclerViewAuctions.setLayoutManager(new LinearLayoutManager(this));
        loadAuctions();
    }

    private void loadAuctions() {
        List<Auction> auctionList = db.getAllAuctions();
        adapter = new AuctionAdapter(this, auctionList, auction -> {
            Intent intent = new Intent(AuctionListActivity.this, AuctionDetailActivity.class);
            intent.putExtra("auctionId", auction.getId());
            startActivity(intent);
        });
        recyclerViewAuctions.setAdapter(adapter);
    }
}
