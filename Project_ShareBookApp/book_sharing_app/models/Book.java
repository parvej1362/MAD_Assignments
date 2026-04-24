package com.example.book_sharing_app.models;

public class Book {
    private String id;
    private String title;
    private String description;
    private double price;
    private String sellerId;
    private String imageUrl;
    private String status; // "Available", "Sold"
    private String type; // "sale", "auction"

    public Book() {
    }

    public Book(String id, String title, String description, double price, String sellerId, String imageUrl, String status, String type) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.price = price;
        this.sellerId = sellerId;
        this.imageUrl = imageUrl;
        this.status = status;
        this.type = type;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getTitle() { return title; }
    public void setTitle(String title) { this.title = title; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public double getPrice() { return price; }
    public void setPrice(double price) { this.price = price; }

    public String getSellerId() { return sellerId; }
    public void setSellerId(String sellerId) { this.sellerId = sellerId; }

    public String getImageUrl() { return imageUrl; }
    public void setImageUrl(String imageUrl) { this.imageUrl = imageUrl; }

    public String getStatus() { return status; }
    public void setStatus(String status) { this.status = status; }

    public String getType() { return type; }
    public void setType(String type) { this.type = type; }
}
