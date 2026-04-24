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

import java.io.File;
import java.util.List;

public class BookAdapter extends RecyclerView.Adapter<BookAdapter.BookViewHolder> {

    private Context context;
    private List<Book> bookList;
    private OnBookClickListener listener;

    public interface OnBookClickListener {
        void onBookClick(Book book);
        void onEditClick(Book book);
        void onDeleteClick(Book book);
    }

    public BookAdapter(Context context, List<Book> bookList, OnBookClickListener listener) {
        this.context = context;
        this.bookList = bookList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public BookViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_book, parent, false);
        return new BookViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull BookViewHolder holder, int position) {
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

        holder.itemView.setOnClickListener(v -> listener.onBookClick(book));
    }

    public void updateList(List<Book> newList) {
        this.bookList = newList;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return bookList.size();
    }

    public static class BookViewHolder extends RecyclerView.ViewHolder {
        ImageView ivBook;
        TextView tvTitle, tvPrice;

        public BookViewHolder(@NonNull View itemView) {
            super(itemView);
            ivBook = itemView.findViewById(R.id.ivBook);
            tvTitle = itemView.findViewById(R.id.tvBookTitle);
            tvPrice = itemView.findViewById(R.id.tvBookPrice);
        }
    }
}
