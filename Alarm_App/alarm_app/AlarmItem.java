package com.example.alarm_app;

public class AlarmItem {
    private int id;
    private int hour;
    private int minute;
    private int year;
    private int month;
    private int day;
    private boolean isVibrate;

    public AlarmItem(int id, int hour, int minute, int year, int month, int day, boolean isVibrate) {
        this.id = id;
        this.hour = hour;
        this.minute = minute;
        this.year = year;
        this.month = month;
        this.day = day;
        this.isVibrate = isVibrate;
    }

    public int getId() { return id; }
    public int getHour() { return hour; }
    public int getMinute() { return minute; }
    public int getYear() { return year; }
    public int getMonth() { return month; }
    public int getDay() { return day; }
    public boolean isVibrate() { return isVibrate; }
}
