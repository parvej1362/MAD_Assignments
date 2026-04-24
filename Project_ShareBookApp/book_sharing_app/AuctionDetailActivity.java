package com.example.book_sharing_app;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;

import com.example.book_sharing_app.models.Auction;
import com.example.book_sharing_app.models.Bid;
import com.example.book_sharing_app.models.Book;
import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class AuctionDetailActivity extends AppCompatActivity {

    private ImageView ivBook, btnBack;
    private TextView tvTitle, tvEnds, tvStartPrice, tvCurrentBid, tvDesc, tvTimer;
    private MaterialButton btnAddBid, btnViewBids;
    private DatabaseHelper db;
    private String auctionId, currentUserId;
    private Auction auction;
    private Book book;
    private CountDownTimer countDownTimer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_auction_detail);

        db = new DatabaseHelper(this);
        auctionId = getIntent().getStringExtra("auctionId");
        SharedPreferences pref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        currentUserId = pref.getString("userId", "");

        ivBook = findViewById(R.id.ivAuctionDetailBook);
        btnBack = findViewById(R.id.btnBack);
        tvTitle = findViewById(R.id.tvAuctionDetailTitle);
        tvEnds = findViewById(R.id.tvAuctionDetailEnds);
        tvStartPrice = findViewById(R.id.tvAuctionDetailStartPrice);
        tvCurrentBid = findViewById(R.id.tvAuctionDetailCurrentBid);
        tvDesc = findViewById(R.id.tvAuctionDetailDesc);
        btnAddBid = findViewById(R.id.btnAddBid);
        btnViewBids = findViewById(R.id.btnViewBids);
        
        // Use tvEnds as the timer display too or update layout if needed.
        // For now I'll use tvEnds to show both date and remaining time.

        btnBack.setOnClickListener(v -> onBackPressed());

        loadAuctionDetails();

        btnAddBid.setOnClickListener(v -> showBidDialog());
        btnViewBids.setOnClickListener(v -> {
            Intent intent = new Intent(AuctionDetailActivity.this, BidHistoryActivity.class);
            intent.putExtra("auctionId", auctionId);
            startActivity(intent);
        });
    }

    private void loadAuctionDetails() {
        auction = null;
        for (Auction a : db.getAllAuctions()) {
            if (a.getId().equals(auctionId)) {
                auction = a;
                break;
            }
        }

        if (auction != null) {
            book = db.getBookById(auction.getBookId());
            if (book != null) {
                tvTitle.setText(book.getTitle());
                tvDesc.setText(book.getDescription());
                tvStartPrice.setText("₹ " + (int)auction.getStartingPrice());
                
                // Load Image
                if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
                    File imgFile = new File(book.getImageUrl());
                    if (imgFile.exists()) {
                        Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                        ivBook.setImageBitmap(bitmap);
                    }
                }
            }

            startTimer(auction.getEndDateTime());
            updateCurrentBidDisplay();
        }
    }

    private void startTimer(String endDateTimeStr) {
        try {
            SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            Date endDate = sdf.parse(endDateTimeStr);
            if (endDate == null) return;

            long millisInFuture = endDate.getTime() - System.currentTimeMillis();

            if (millisInFuture > 0) {
                countDownTimer = new CountDownTimer(millisInFuture, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        long hours = millisUntilFinished / (1000 * 60 * 60);
                        long mins = (millisUntilFinished / (1000 * 60)) % 60;
                        long secs = (millisUntilFinished / 1000) % 60;
                        tvEnds.setText("Closing in: " + String.format("%02dh %02dm %02ds", hours, mins, secs));
                    }

                    @Override
                    public void onFinish() {
                        tvEnds.setText("AUCTION CLOSED");
                        tvEnds.setTextColor(android.graphics.Color.RED);
                        db.updateAuctionStatus(auctionId, "CLOSED");
                        btnAddBid.setEnabled(false);
                        btnAddBid.setText("CLOSED");
                    }
                }.start();
            } else {
                tvEnds.setText("AUCTION CLOSED");
                tvEnds.setTextColor(android.graphics.Color.RED);
                db.updateAuctionStatus(auctionId, "CLOSED");
                btnAddBid.setEnabled(false);
                btnAddBid.setText("CLOSED");
            }
        } catch (Exception e) {
            e.printStackTrace();
            tvEnds.setText("Ends: " + endDateTimeStr);
        }
    }

    private void updateCurrentBidDisplay() {
        Bid highestBid = db.getHighestBid(auctionId);
        if (highestBid != null) {
            tvCurrentBid.setText("₹ " + (int)highestBid.getAmount());
        } else {
            tvCurrentBid.setText("₹ " + (int)auction.getStartingPrice());
        }
    }

    private void showBidDialog() {
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_bid);
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        TextView tvClose = dialog.findViewById(R.id.tvCloseDialog);
        TextInputEditText etAmount = dialog.findViewById(R.id.etBidAmount);
        MaterialButton btnSubmit = dialog.findViewById(R.id.btnSubmitBid);

        tvClose.setOnClickListener(v -> dialog.dismiss());

        btnSubmit.setOnClickListener(v -> {
            String amountStr = etAmount.getText().toString().trim();
            if (amountStr.isEmpty()) {
                etAmount.setError("Price Cannot be Empty");
                return;
            }

            double bidAmount = Double.parseDouble(amountStr);
            Bid highest = db.getHighestBid(auctionId);
            double minBid = (highest != null) ? highest.getAmount() : auction.getStartingPrice();

            if (bidAmount <= minBid) {
                Toast.makeText(this, "Bid must be higher than ₹" + (int)minBid, Toast.LENGTH_SHORT).show();
                return;
            }

            String currentTime = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault()).format(new Date());
            if (db.placeBid(auctionId, currentUserId, bidAmount, currentTime)) {
                Toast.makeText(this, "Bid Placed Successfully!", Toast.LENGTH_SHORT).show();
                updateCurrentBidDisplay();
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Failed to place bid", Toast.LENGTH_SHORT).show();
            }
        });

        dialog.show();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }
}
