package com.example.book_sharing_app.models;

public class Auction {
    private String id;
    private String bookId;
    private String sellerId;
    private double startingPrice;
    private String endDateTime;
    private String status;

    public Auction(String id, String bookId, String sellerId, double startingPrice, String endDateTime, String status) {
        this.id = id;
        this.bookId = bookId;
        this.sellerId = sellerId;
        this.startingPrice = startingPrice;
        this.endDateTime = endDateTime;
        this.status = status;
    }

    public String getId() { return id; }
    public String getBookId() { return bookId; }
    public String getSellerId() { return sellerId; }
    public double getStartingPrice() { return startingPrice; }
    public String getEndDateTime() { return endDateTime; }
    public String getStatus() { return status; }
}
