package com.example.book_sharing_app.models;

public class User {
    private String id;
    private String name;
    private String mobile;
    private String email;
    private String address;
    private String role; // "admin" or "user"
    private boolean isBlocked;

    public User() {
    }

    public User(String id, String name, String mobile, String email, String address, String role, boolean isBlocked) {
        this.id = id;
        this.name = name;
        this.mobile = mobile;
        this.email = email;
        this.address = address;
        this.role = role;
        this.isBlocked = isBlocked;
    }

    public String getId() { return id; }
    public void setId(String id) { this.id = id; }

    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getMobile() { return mobile; }
    public void setMobile(String mobile) { this.mobile = mobile; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }

    public String getRole() { return role; }
    public void setRole(String role) { this.role = role; }

    public boolean isBlocked() { return isBlocked; }
    public void setBlocked(boolean blocked) { isBlocked = blocked; }
}
