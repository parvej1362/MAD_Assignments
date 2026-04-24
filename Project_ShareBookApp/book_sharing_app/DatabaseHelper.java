package com.example.book_sharing_app;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.book_sharing_app.models.Book;
import com.example.book_sharing_app.models.User;
import com.example.book_sharing_app.models.Auction;
import com.example.book_sharing_app.models.Bid;

import java.util.ArrayList;
import java.util.List;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String DATABASE_NAME = "BookSharing.db";
    private static final int DATABASE_VERSION = 4;

    // Users table
    public static final String TABLE_USERS = "users";
    public static final String COL_USER_ID = "id";
    public static final String COL_USER_NAME = "name";
    public static final String COL_USER_MOBILE = "mobile";
    public static final String COL_USER_EMAIL = "email";
    public static final String COL_USER_ADDRESS = "address";
    public static final String COL_USER_ROLE = "role";
    public static final String COL_USER_PASSWORD = "password";
    public static final String COL_USER_IS_BLOCKED = "is_blocked";

    // Books table
    public static final String TABLE_BOOKS = "books";
    public static final String COL_BOOK_ID = "id";
    public static final String COL_BOOK_TITLE = "title";
    public static final String COL_BOOK_DESCRIPTION = "description";
    public static final String COL_BOOK_PRICE = "price";
    public static final String COL_BOOK_SELLER_ID = "seller_id";
    public static final String COL_BOOK_IMAGE_URL = "image_url";
    public static final String COL_BOOK_STATUS = "status";
    public static final String COL_BOOK_TYPE = "type";

    // Cart table
    public static final String TABLE_CART = "cart";
    public static final String COL_CART_ID = "id";
    public static final String COL_CART_USER_ID = "user_id";
    public static final String COL_CART_BOOK_ID = "book_id";

    // Auctions table
    public static final String TABLE_AUCTIONS = "auctions";
    public static final String COL_AUCTION_ID = "id";
    public static final String COL_AUCTION_BOOK_ID = "book_id";
    public static final String COL_AUCTION_SELLER_ID = "seller_id";
    public static final String COL_AUCTION_STARTING_PRICE = "starting_price";
    public static final String COL_AUCTION_END_DATETIME = "end_datetime";
    public static final String COL_AUCTION_STATUS = "status"; // OPEN, CLOSED

    // Bids table
    public static final String TABLE_BIDS = "bids";
    public static final String COL_BID_ID = "id";
    public static final String COL_BID_AUCTION_ID = "auction_id";
    public static final String COL_BID_USER_ID = "user_id";
    public static final String COL_BID_AMOUNT = "bid_amount";
    public static final String COL_BID_TIME = "bid_time";

    public DatabaseHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String createUsersTable = "CREATE TABLE " + TABLE_USERS + " (" +
                COL_USER_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_USER_NAME + " TEXT, " +
                COL_USER_MOBILE + " TEXT, " +
                COL_USER_EMAIL + " TEXT UNIQUE, " +
                COL_USER_ADDRESS + " TEXT, " +
                COL_USER_ROLE + " TEXT, " +
                COL_USER_PASSWORD + " TEXT, " +
                COL_USER_IS_BLOCKED + " INTEGER DEFAULT 0)";

        String createBooksTable = "CREATE TABLE " + TABLE_BOOKS + " (" +
                COL_BOOK_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_BOOK_TITLE + " TEXT, " +
                COL_BOOK_DESCRIPTION + " TEXT, " +
                COL_BOOK_PRICE + " REAL, " +
                COL_BOOK_SELLER_ID + " TEXT, " +
                COL_BOOK_IMAGE_URL + " TEXT, " +
                COL_BOOK_STATUS + " TEXT, " +
                COL_BOOK_TYPE + " TEXT)";

        String createCartTable = "CREATE TABLE " + TABLE_CART + " (" +
                COL_CART_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_CART_USER_ID + " TEXT, " +
                COL_CART_BOOK_ID + " TEXT)";

        String createAuctionsTable = "CREATE TABLE " + TABLE_AUCTIONS + " (" +
                COL_AUCTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_AUCTION_BOOK_ID + " TEXT, " +
                COL_AUCTION_SELLER_ID + " TEXT, " +
                COL_AUCTION_STARTING_PRICE + " REAL, " +
                COL_AUCTION_END_DATETIME + " TEXT, " +
                COL_AUCTION_STATUS + " TEXT DEFAULT 'OPEN')";

        String createBidsTable = "CREATE TABLE " + TABLE_BIDS + " (" +
                COL_BID_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL_BID_AUCTION_ID + " TEXT, " +
                COL_BID_USER_ID + " TEXT, " +
                COL_BID_AMOUNT + " REAL, " +
                COL_BID_TIME + " TEXT)";

        db.execSQL(createUsersTable);
        db.execSQL(createBooksTable);
        db.execSQL(createCartTable);
        db.execSQL(createAuctionsTable);
        db.execSQL(createBidsTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        if (oldVersion < 4) {
            String createAuctionsTable = "CREATE TABLE " + TABLE_AUCTIONS + " (" +
                    COL_AUCTION_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_AUCTION_BOOK_ID + " TEXT, " +
                    COL_AUCTION_SELLER_ID + " TEXT, " +
                    COL_AUCTION_STARTING_PRICE + " REAL, " +
                    COL_AUCTION_END_DATETIME + " TEXT, " +
                    COL_AUCTION_STATUS + " TEXT DEFAULT 'OPEN')";

            String createBidsTable = "CREATE TABLE " + TABLE_BIDS + " (" +
                    COL_BID_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                    COL_BID_AUCTION_ID + " TEXT, " +
                    COL_BID_USER_ID + " TEXT, " +
                    COL_BID_AMOUNT + " REAL, " +
                    COL_BID_TIME + " TEXT)";
            
            db.execSQL(createAuctionsTable);
            db.execSQL(createBidsTable);
        }
    }

    // --- User Operations ---

    public boolean registerUser(User user, String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_NAME, user.getName());
        values.put(COL_USER_MOBILE, user.getMobile());
        values.put(COL_USER_EMAIL, user.getEmail());
        values.put(COL_USER_ADDRESS, user.getAddress());
        values.put(COL_USER_ROLE, user.getRole());
        values.put(COL_USER_PASSWORD, password);
        values.put(COL_USER_IS_BLOCKED, user.isBlocked() ? 1 : 0);

        long result = db.insert(TABLE_USERS, null, values);
        return result != -1;
    }

    public User loginUser(String email, String password) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COL_USER_EMAIL + "=? AND " + COL_USER_PASSWORD + "=?",
                new String[]{email, password}, null, null, null);

        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_MOBILE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_ADDRESS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_ROLE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_IS_BLOCKED)) == 1
            );
            cursor.close();
            return user;
        }
        return null;
    }

    public User getUserById(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_USERS, null, COL_USER_ID + "=?", new String[]{id}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            User user = new User(
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_NAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_MOBILE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_EMAIL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_ADDRESS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_ROLE)),
                    cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_IS_BLOCKED)) == 1
            );
            cursor.close();
            return user;
        }
        return null;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_USERS + " WHERE " + COL_USER_ROLE + "='user'", null);

        if (cursor.moveToFirst()) {
            do {
                User user = new User(
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_NAME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_MOBILE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_EMAIL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_ADDRESS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_USER_ROLE)),
                        cursor.getInt(cursor.getColumnIndexOrThrow(COL_USER_IS_BLOCKED)) == 1
                );
                users.add(user);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return users;
    }

    public boolean updateUserStatus(String userId, boolean isBlocked) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_IS_BLOCKED, isBlocked ? 1 : 0);
        int result = db.update(TABLE_USERS, values, COL_USER_ID + "=?", new String[]{userId});
        return result > 0;
    }

    public boolean updateUserPassword(String userId, String newPassword) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_USER_PASSWORD, newPassword);
        int result = db.update(TABLE_USERS, values, COL_USER_ID + "=?", new String[]{userId});
        return result > 0;
    }

    // --- Book Operations ---

    public boolean addBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_BOOK_TITLE, book.getTitle());
        values.put(COL_BOOK_DESCRIPTION, book.getDescription());
        values.put(COL_BOOK_PRICE, book.getPrice());
        values.put(COL_BOOK_SELLER_ID, book.getSellerId());
        values.put(COL_BOOK_IMAGE_URL, book.getImageUrl());
        values.put(COL_BOOK_STATUS, book.getStatus());
        values.put(COL_BOOK_TYPE, book.getType());

        long result = db.insert(TABLE_BOOKS, null, values);
        return result != -1;
    }

    public List<Book> getAllBooks() {
        List<Book> books = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_BOOKS, null);

        if (cursor.moveToFirst()) {
            do {
                Book book = new Book(
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_DESCRIPTION)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COL_BOOK_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_SELLER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_IMAGE_URL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_STATUS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_TYPE))
                );
                books.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return books;
    }

    public Book getBookById(String id) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKS, null, COL_BOOK_ID + "=?", new String[]{id}, null, null, null);
        if (cursor != null && cursor.moveToFirst()) {
            Book book = new Book(
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_TITLE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_DESCRIPTION)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COL_BOOK_PRICE)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_SELLER_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_IMAGE_URL)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_STATUS)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_TYPE))
            );
            cursor.close();
            return book;
        }
        return null;
    }

    public List<Book> getBooksBySeller(String sellerId) {
        List<Book> books = new ArrayList<>();
        if (sellerId == null || sellerId.isEmpty()) return books;
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BOOKS, null, COL_BOOK_SELLER_ID + "=?", new String[]{sellerId}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Book book = new Book(
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_DESCRIPTION)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COL_BOOK_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_SELLER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_IMAGE_URL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_STATUS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_TYPE))
                );
                books.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return books;
    }

    public boolean updateBook(Book book) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_BOOK_TITLE, book.getTitle());
        values.put(COL_BOOK_DESCRIPTION, book.getDescription());
        values.put(COL_BOOK_PRICE, book.getPrice());
        values.put(COL_BOOK_IMAGE_URL, book.getImageUrl());
        values.put(COL_BOOK_STATUS, book.getStatus());
        values.put(COL_BOOK_TYPE, book.getType());

        int result = db.update(TABLE_BOOKS, values, COL_BOOK_ID + "=?", new String[]{book.getId()});
        return result > 0;
    }

    public boolean deleteBook(String bookId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_BOOKS, COL_BOOK_ID + "=?", new String[]{bookId});
        return result > 0;
    }

    // --- Cart Operations ---

    public boolean addToCart(String userId, String bookId) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_CART_USER_ID, userId);
        values.put(COL_CART_BOOK_ID, bookId);
        long result = db.insert(TABLE_CART, null, values);
        return result != -1;
    }

    public boolean isInCart(String userId, String bookId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_CART, null, COL_CART_USER_ID + "=? AND " + COL_CART_BOOK_ID + "=?",
                new String[]{userId, bookId}, null, null, null);
        boolean exists = cursor.getCount() > 0;
        cursor.close();
        return exists;
    }

    public List<Book> getCartBooks(String userId) {
        List<Book> books = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT b.* FROM " + TABLE_BOOKS + " b " +
                "JOIN " + TABLE_CART + " c ON b." + COL_BOOK_ID + " = c." + COL_CART_BOOK_ID +
                " WHERE c." + COL_CART_USER_ID + " = ?";
        Cursor cursor = db.rawQuery(query, new String[]{userId});

        if (cursor.moveToFirst()) {
            do {
                Book book = new Book(
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_TITLE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_DESCRIPTION)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COL_BOOK_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_SELLER_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_IMAGE_URL)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_STATUS)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_BOOK_TYPE))
                );
                books.add(book);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return books;
    }

    public boolean removeFromCart(String userId, String bookId) {
        SQLiteDatabase db = this.getWritableDatabase();
        int result = db.delete(TABLE_CART, COL_CART_USER_ID + "=? AND " + COL_CART_BOOK_ID + "=?", new String[]{userId, bookId});
        return result > 0;
    }

    public void clearCart(String userId) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete(TABLE_CART, COL_CART_USER_ID + "=?", new String[]{userId});
    }

    // --- Auction Operations ---

    public boolean createAuction(String bookId, String sellerId, double startingPrice, String endDateTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_AUCTION_BOOK_ID, bookId);
        values.put(COL_AUCTION_SELLER_ID, sellerId);
        values.put(COL_AUCTION_STARTING_PRICE, startingPrice);
        values.put(COL_AUCTION_END_DATETIME, endDateTime);
        values.put(COL_AUCTION_STATUS, "OPEN");

        long result = db.insert(TABLE_AUCTIONS, null, values);
        return result != -1;
    }

    public List<Auction> getAllAuctions() {
        List<Auction> auctions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM " + TABLE_AUCTIONS, null);

        if (cursor.moveToFirst()) {
            do {
                Auction auction = new Auction(
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_AUCTION_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_AUCTION_BOOK_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_AUCTION_SELLER_ID)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COL_AUCTION_STARTING_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_AUCTION_END_DATETIME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_AUCTION_STATUS))
                );
                auctions.add(auction);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return auctions;
    }

    public List<Auction> getAuctionsBySeller(String sellerId) {
        List<Auction> auctions = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_AUCTIONS, null, COL_AUCTION_SELLER_ID + "=?", new String[]{sellerId}, null, null, null);

        if (cursor.moveToFirst()) {
            do {
                Auction auction = new Auction(
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_AUCTION_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_AUCTION_BOOK_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_AUCTION_SELLER_ID)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COL_AUCTION_STARTING_PRICE)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_AUCTION_END_DATETIME)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_AUCTION_STATUS))
                );
                auctions.add(auction);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return auctions;
    }

    public boolean placeBid(String auctionId, String userId, double amount, String bidTime) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_BID_AUCTION_ID, auctionId);
        values.put(COL_BID_USER_ID, userId);
        values.put(COL_BID_AMOUNT, amount);
        values.put(COL_BID_TIME, bidTime);

        long result = db.insert(TABLE_BIDS, null, values);
        return result != -1;
    }

    public List<Bid> getBidsForAuction(String auctionId) {
        List<Bid> bids = new ArrayList<>();
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BIDS, null, COL_BID_AUCTION_ID + "=?", new String[]{auctionId}, null, null, COL_BID_AMOUNT + " DESC");

        if (cursor.moveToFirst()) {
            do {
                Bid bid = new Bid(
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_BID_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_BID_AUCTION_ID)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_BID_USER_ID)),
                        cursor.getDouble(cursor.getColumnIndexOrThrow(COL_BID_AMOUNT)),
                        cursor.getString(cursor.getColumnIndexOrThrow(COL_BID_TIME))
                );
                bids.add(bid);
            } while (cursor.moveToNext());
        }
        cursor.close();
        return bids;
    }

    public Bid getHighestBid(String auctionId) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_BIDS, null, COL_BID_AUCTION_ID + "=?", new String[]{auctionId}, null, null, COL_BID_AMOUNT + " DESC", "1");
        if (cursor != null && cursor.moveToFirst()) {
            Bid bid = new Bid(
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_BID_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_BID_AUCTION_ID)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_BID_USER_ID)),
                    cursor.getDouble(cursor.getColumnIndexOrThrow(COL_BID_AMOUNT)),
                    cursor.getString(cursor.getColumnIndexOrThrow(COL_BID_TIME))
            );
            cursor.close();
            return bid;
        }
        return null;
    }

    public void updateAuctionStatus(String auctionId, String status) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(COL_AUCTION_STATUS, status);
        db.update(TABLE_AUCTIONS, values, COL_AUCTION_ID + "=?", new String[]{auctionId});
    }
}
