package com.example.book_sharing_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_sharing_app.models.Bid;
import com.example.book_sharing_app.models.User;

import java.util.List;

public class BidAdapter extends RecyclerView.Adapter<BidAdapter.BidViewHolder> {

    private Context context;
    private List<Bid> bidList;
    private DatabaseHelper db;
    private boolean isAuctionClosed;

    public BidAdapter(Context context, List<Bid> bidList, boolean isAuctionClosed) {
        this.context = context;
        this.bidList = bidList;
        this.db = new DatabaseHelper(context);
        this.isAuctionClosed = isAuctionClosed;
    }

    @NonNull
    @Override
    public BidViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_bid, parent, false);
        return new BidViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BidViewHolder holder, int position) {
        Bid bid = bidList.get(position);
        User user = db.getUserById(bid.getUserId());

        if (user != null) {
            holder.tvBidderName.setText(user.getName());
            holder.tvBidderContact.setText(user.getMobile());
        }

        holder.tvBidAmount.setText("₹ " + (int)bid.getAmount());

        // Highest bid is at position 0 (sorted DESC)
        if (isAuctionClosed && position == 0) {
            holder.layoutWinner.setVisibility(View.VISIBLE);
        } else {
            holder.layoutWinner.setVisibility(View.GONE);
        }
    }

    @Override
    public int getItemCount() {
        return bidList.size();
    }

    public static class BidViewHolder extends RecyclerView.ViewHolder {
        TextView tvBidderName, tvBidAmount, tvBidderContact;
        LinearLayout layoutWinner;

        public BidViewHolder(@NonNull View itemView) {
            super(itemView);
            tvBidderName = itemView.findViewById(R.id.tvBidderName);
            tvBidAmount = itemView.findViewById(R.id.tvBidAmount);
            tvBidderContact = itemView.findViewById(R.id.tvBidderContact);
            layoutWinner = itemView.findViewById(R.id.layoutWinner);
        }
    }
}
