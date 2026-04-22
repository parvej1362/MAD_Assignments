package com.example.alarm_app;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;
import java.util.Locale;

public class AlarmAdapter extends RecyclerView.Adapter<AlarmAdapter.ViewHolder> {

    private List<AlarmItem> alarmList;

    public AlarmAdapter(List<AlarmItem> alarmList) {
        this.alarmList = alarmList;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_alarm, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        AlarmItem item = alarmList.get(position);

        String amPm = item.getHour() >= 12 ? "PM" : "AM";
        int hour12 = item.getHour() % 12;
        if (hour12 == 0) hour12 = 12;

        String formattedTime = String.format(Locale.getDefault(), "%02d:%02d %s", hour12, item.getMinute(), amPm);
        String formattedDate = String.format(Locale.getDefault(), "%04d-%02d-%02d", item.getYear(), item.getMonth() + 1, item.getDay());

        holder.tvTime.setText(formattedTime);
        holder.tvDate.setText(formattedDate);
        holder.tvVibrate.setText("Vibrate: " + (item.isVibrate() ? "On" : "Off"));
    }

    @Override
    public int getItemCount() {
        return alarmList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvTime;
        TextView tvDate;
        TextView tvVibrate;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvTime = itemView.findViewById(R.id.tv_item_time);
            tvDate = itemView.findViewById(R.id.tv_item_date);
            tvVibrate = itemView.findViewById(R.id.tv_item_vibrate);
        }
    }
}
