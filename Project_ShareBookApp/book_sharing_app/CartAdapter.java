package com.example.book_sharing_app;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_sharing_app.models.Book;
import com.google.android.material.button.MaterialButton;

import java.io.File;
import java.util.List;

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.CartViewHolder> {

    private Context context;
    private List<Book> bookList;
    private OnRemoveClickListener listener;

    public interface OnRemoveClickListener {
        void onRemove(Book book);
    }

    public CartAdapter(Context context, List<Book> bookList, OnRemoveClickListener listener) {
        this.context = context;
        this.bookList = bookList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public CartViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_cart, parent, false);
        return new CartViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CartViewHolder holder, int position) {
        Book book = bookList.get(position);
        holder.tvTitle.setText(book.getTitle());
        holder.tvPrice.setText("₹ " + book.getPrice());

        try {
            if (book.getImageUrl() != null && !book.getImageUrl().isEmpty()) {
                if (book.getImageUrl().startsWith("content://")) {
                    holder.ivBook.setImageURI(Uri.parse(book.getImageUrl()));
                } else {
                    File file = new File(book.getImageUrl());
                    if (file.exists()) {
                        holder.ivBook.setImageURI(Uri.fromFile(file));
                    } else {
                        holder.ivBook.setImageResource(R.drawable.ic_logo);
                    }
                }
            } else {
                holder.ivBook.setImageResource(R.drawable.ic_logo);
            }
        } catch (Exception e) {
            holder.ivBook.setImageResource(R.drawable.ic_logo);
        }

        holder.btnRemove.setOnClickListener(v -> listener.onRemove(book));
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class CartViewHolder extends RecyclerView.ViewHolder {
        ImageView ivBook;
        TextView tvTitle, tvPrice;
        MaterialButton btnRemove;

        public CartViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBook = itemView.findViewById(R.id.ivBook);
            tvTitle = itemView.findViewById(R.id.tvBookTitle);
            tvPrice = itemView.findViewById(R.id.tvBookPrice);
            btnRemove = itemView.findViewById(R.id.btnRemoveItem);
        }
    }
}
