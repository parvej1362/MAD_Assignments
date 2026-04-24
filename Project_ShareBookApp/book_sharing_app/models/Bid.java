package com.example.book_sharing_app.models;

public class Bid {
    private String id;
    private String auctionId;
    private String userId;
    private double amount;
    private String bidTime;

    public Bid(String id, String auctionId, String userId, double amount, String bidTime) {
        this.id = id;
        this.auctionId = auctionId;
        this.userId = userId;
        this.amount = amount;
        this.bidTime = bidTime;
    }

    public String getId() { return id; }
    public String getAuctionId() { return auctionId; }
    public String getUserId() { return userId; }
    public double getAmount() { return amount; }
    public String getBidTime() { return bidTime; }
}
