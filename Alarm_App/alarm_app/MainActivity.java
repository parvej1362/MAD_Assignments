package com.example.alarm_app;

import android.app.AlarmManager;
import android.app.DatePickerDialog;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.provider.Settings;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.switchmaterial.SwitchMaterial;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private int selectedYear, selectedMonth, selectedDay;
    private int selectedHour = -1, selectedMinute = -1;
    private boolean isDateSelected = false;

    private MaterialButton btnSelectDate, btnSelectTime, btnSetAlarm;
    private SwitchMaterial switchVibrate;
    private RecyclerView rvAlarms;
    
    private AlarmAdapter alarmAdapter;
    private List<AlarmItem> alarmList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setupListeners();
        checkPermissions();
    }

    private void checkPermissions() {
        // Request Notification Permission for Android 13+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            if (androidx.core.content.ContextCompat.checkSelfPermission(this, android.Manifest.permission.POST_NOTIFICATIONS) != android.content.pm.PackageManager.PERMISSION_GRANTED) {
                androidx.core.app.ActivityCompat.requestPermissions(this, new String[]{android.Manifest.permission.POST_NOTIFICATIONS}, 101);
            }
        }

        // Request Exact Alarm Permission for Android 12+
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
            if (alarmManager != null && !alarmManager.canScheduleExactAlarms()) {
                Intent intent = new Intent(Settings.ACTION_REQUEST_SCHEDULE_EXACT_ALARM);
                intent.setData(android.net.Uri.parse("package:" + getPackageName()));
                startActivity(intent);
            }
        }
    }

    private void initViews() {
        btnSelectDate = findViewById(R.id.btn_select_date);
        btnSelectTime = findViewById(R.id.btn_select_time);
        switchVibrate = findViewById(R.id.switch_vibrate);
        btnSetAlarm = findViewById(R.id.btn_set_alarm);
        rvAlarms = findViewById(R.id.rv_alarms);

        rvAlarms.setLayoutManager(new LinearLayoutManager(this));
        alarmAdapter = new AlarmAdapter(alarmList);
        rvAlarms.setAdapter(alarmAdapter);
    }

    private void setupListeners() {
        btnSelectDate.setOnClickListener(v -> showDatePicker());
        btnSelectTime.setOnClickListener(v -> showTimePicker());
        btnSetAlarm.setOnClickListener(v -> setAlarm());
    }

    private void showDatePicker() {
        final Calendar c = Calendar.getInstance();
        int year = c.get(Calendar.YEAR);
        int month = c.get(Calendar.MONTH);
        int day = c.get(Calendar.DAY_OF_MONTH);

        DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                (view, year1, monthOfYear, dayOfMonth) -> {
                    selectedYear = year1;
                    selectedMonth = monthOfYear;
                    selectedDay = dayOfMonth;
                    isDateSelected = true;
                    btnSelectDate.setText(String.format(Locale.getDefault(), "%04d-%02d-%02d", selectedYear, selectedMonth + 1, selectedDay));
                }, year, month, day);

        // the requirement: only dates from today and after today
        datePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);
        datePickerDialog.show();
    }

    private void showTimePicker() {
        final Calendar c = Calendar.getInstance();
        int hour = c.get(Calendar.HOUR_OF_DAY);
        int minute = c.get(Calendar.MINUTE);

        TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                (view, hourOfDay, minute1) -> {
                    selectedHour = hourOfDay;
                    selectedMinute = minute1;
                    
                    String amPm = hourOfDay >= 12 ? "PM" : "AM";
                    int hour12 = hourOfDay % 12;
                    if (hour12 == 0) hour12 = 12;
                    
                    btnSelectTime.setText(String.format(Locale.getDefault(), "%02d:%02d %s", hour12, selectedMinute, amPm));
                }, hour, minute, false); // false for 12-hour AM/PM format

        timePickerDialog.show();
    }

    // Permissions are now checked in checkPermissions()

    private void setAlarm() {
        if (!isDateSelected) {
            Toast.makeText(this, "Please select a date", Toast.LENGTH_SHORT).show();
            return;
        }
        if (selectedHour == -1 || selectedMinute == -1) {
            Toast.makeText(this, "Please select a time", Toast.LENGTH_SHORT).show();
            return;
        }

        Calendar alarmCalendar = Calendar.getInstance();
        alarmCalendar.set(selectedYear, selectedMonth, selectedDay, selectedHour, selectedMinute, 0);

        if (alarmCalendar.getTimeInMillis() <= System.currentTimeMillis()) {
            Toast.makeText(this, "Alarm cannot be set in the past", Toast.LENGTH_SHORT).show();
            return;
        }

        int requestCode = (int) System.currentTimeMillis(); // unique ID
        boolean isVibrate = switchVibrate.isChecked();

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlarmReceiver.class);
        intent.putExtra("isVibrate", isVibrate);

        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, requestCode, intent,
                PendingIntent.FLAG_UPDATE_CURRENT | PendingIntent.FLAG_IMMUTABLE);

        if (alarmManager != null) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                alarmManager.setExactAndAllowWhileIdle(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), pendingIntent);
            } else {
                alarmManager.setExact(AlarmManager.RTC_WAKEUP, alarmCalendar.getTimeInMillis(), pendingIntent);
            }
            Toast.makeText(this, "Alarm set successfully", Toast.LENGTH_SHORT).show();

            // Add to list and update UI
            AlarmItem newItem = new AlarmItem(requestCode, selectedHour, selectedMinute, selectedYear, selectedMonth, selectedDay, isVibrate);
            alarmList.add(newItem);
            alarmAdapter.notifyItemInserted(alarmList.size() - 1);
            
            // Reset state
            btnSelectTime.setText("Select Time");
            btnSelectDate.setText("Select Date");
            isDateSelected = false;
            selectedHour = -1;
            selectedMinute = -1;
            switchVibrate.setChecked(false);
        }
    }
}