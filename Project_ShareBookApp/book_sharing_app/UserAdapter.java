package com.example.book_sharing_app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.book_sharing_app.models.User;
import com.google.android.material.button.MaterialButton;

import java.util.List;

public class UserAdapter extends RecyclerView.Adapter<UserAdapter.UserViewHolder> {

    private Context context;
    private List<User> userList;
    private OnUserUpdateListener listener;

    public interface OnUserUpdateListener {
        void onUpdateStatus(User user, boolean isBlocked);
    }

    public UserAdapter(Context context, List<User> userList, OnUserUpdateListener listener) {
        this.context = context;
        this.userList = userList;
        this.listener = listener;
    }

    @NonNull
    @Override
    public UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_user, parent, false);
        return new UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UserViewHolder holder, int position) {
        User user = userList.get(position);
        holder.tvName.setText(user.getName());
        holder.tvContact.setText("Contact: " + user.getMobile());
        holder.tvEmail.setText("Email ID: " + user.getEmail());
        holder.tvAddress.setText("Address: " + user.getAddress());

        String[] statuses = {"Un-Blocked", "Blocked"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(context, android.R.layout.simple_dropdown_item_1line, statuses);
        holder.spinnerStatus.setAdapter(adapter);
        holder.spinnerStatus.setText(user.isBlocked() ? "Blocked" : "Un-Blocked", false);

        holder.btnUpdate.setOnClickListener(v -> {
            boolean isBlocked = holder.spinnerStatus.getText().toString().equals("Blocked");
            listener.onUpdateStatus(user, isBlocked);
        });
    }

    @Override
    public int getItemCount() {
        return userList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        TextView tvName, tvContact, tvEmail, tvAddress;
        AutoCompleteTextView spinnerStatus;
        MaterialButton btnUpdate;

        public UserViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tvUserName);
            tvContact = itemView.findViewById(R.id.tvUserContact);
            tvEmail = itemView.findViewById(R.id.tvUserEmail);
            tvAddress = itemView.findViewById(R.id.tvUserAddress);
            spinnerStatus = itemView.findViewById(R.id.spinnerStatus);
            btnUpdate = itemView.findViewById(R.id.btnUpdateStatus);
        }
    }
}
