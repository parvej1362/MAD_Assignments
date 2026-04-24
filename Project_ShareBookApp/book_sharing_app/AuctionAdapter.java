package com.example.book_sharing_app;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_sharing_app.models.Auction;
import com.example.book_sharing_app.models.Book;
import com.google.android.material.card.MaterialCardView;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class AuctionAdapter extends RecyclerView.Adapter<AuctionAdapter.AuctionViewHolder> {

    private Context context;
    private List<Auction> auctionList;
    private DatabaseHelper db;
    private OnAuctionClickListener listener;

    public interface OnAuctionClickListener {
        void onAuctionClick(Auction auction);
    }

    public AuctionAdapter(Context context, List<Auction> auctionList, OnAuctionClickListener listener) {
        this.context = context;
        this.auctionList = auctionList;
        this.listener = listener;
        this.db = new DatabaseHelper(context);
        sortAuctions();
    }

    private void sortAuctions() {
        Collections.sort(auctionList, (a1, a2) -> {
            if (a1.getStatus().equals("OPEN") && a2.getStatus().equals("CLOSED")) return -1;
            if (a1.getStatus().equals("CLOSED") && a2.getStatus().equals("OPEN")) return 1;
            return 0;
        });
    }

    @NonNull
    @Override
    public AuctionViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_auction, parent, false);
        return new AuctionViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull AuctionViewHolder holder, int position) {
        Auction auction = auctionList.get(position);
        Book book = db.getBookById(auction.getBookId());

        if (book != null) {
            holder.tvAuctionTitle.setText(book.getTitle());
            holder.tvStartingBid.setText("₹ " + (int)auction.getStartingPrice());

            // Load Image
            if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
                File imgFile = new File(book.getImageUrl());
                if (imgFile.exists()) {
                    Bitmap bitmap = BitmapFactory.decodeFile(imgFile.getAbsolutePath());
                    holder.ivAuctionBook.setImageBitmap(bitmap);
                }
            }
        }

        holder.tvAuctionStatus.setText("Status: " + auction.getStatus());
        
        if (auction.getStatus().equals("OPEN")) {
            holder.tvAuctionStatus.setTextColor(Color.parseColor("#2E7D32")); // Green
            holder.cardView.setCardBackgroundColor(Color.parseColor("#E8F5E9")); // Light Greenish
        } else {
            holder.tvAuctionStatus.setTextColor(Color.parseColor("#C62828")); // Red
            holder.cardView.setCardBackgroundColor(Color.WHITE);
        }

        // Format date for display
        String displayDateTime = auction.getEndDateTime();
        try {
            SimpleDateFormat sdfIn = new SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.getDefault());
            SimpleDateFormat sdfOut = new SimpleDateFormat("dd MMM yyyy, hh:mm a", Locale.getDefault());
            Date date = sdfIn.parse(auction.getEndDateTime());
            if (date != null) {
                displayDateTime = sdfOut.format(date);
            }
        } catch (Exception e) {
            // keep raw if error
        }
        holder.tvAuctionEnds.setText("Ends: " + displayDateTime);

        holder.itemView.setOnClickListener(v -> listener.onAuctionClick(auction));
    }

    @Override
    public int getItemCount() {
        return auctionList.size();
    }

    public static class AuctionViewHolder extends RecyclerView.ViewHolder {
        ImageView ivAuctionBook;
        TextView tvAuctionTitle, tvAuctionStatus, tvAuctionEnds, tvStartingBid;
        MaterialCardView cardView;

        public AuctionViewHolder(@NonNull View itemView) {
            super(itemView);
            cardView = (MaterialCardView) itemView;
            ivAuctionBook = itemView.findViewById(R.id.ivAuctionBook);
            tvAuctionTitle = itemView.findViewById(R.id.tvAuctionTitle);
            tvAuctionStatus = itemView.findViewById(R.id.tvAuctionStatus);
            tvAuctionEnds = itemView.findViewById(R.id.tvAuctionEnds);
            tvStartingBid = itemView.findViewById(R.id.tvStartingBid);
        }
    }
}
