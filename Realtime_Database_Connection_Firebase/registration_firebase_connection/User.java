package com.example.registration_firebase_connection;

public class User {
    public String id, name, email, password, address;

    // Default constructor required for calls to DataSnapshot.getValue(User.class)
    public User() {}

    public User(String id, String name, String email, String password, String address) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.password = password;
        this.address = address;
    }
}
