package com.mhyhre.zone_1024.utils;

public class TimeAndDate {

    private final int seconds;
    private final int minutes;
    private final int hours;
    
    private final int day;
    private final int month;
    private final int year;
    
    public TimeAndDate(int seconds, int minutes, int hours, int day, int month, int year) {
        super();
        this.seconds = seconds;
        this.minutes = minutes;
        this.hours = hours;
        this.day = day;
        this.month = month;
        this.year = year;
    }

    public int getSeconds() {
        return seconds;
    }

    public int getMinutes() {
        return minutes;
    }

    public int getHours() {
        return hours;
    }

    public int getDay() {
        return day;
    }

    public int getMonth() {
        return month;
    }

    public int getYear() {
        return year;
    }

    @Override
    public String toString() {
        String result = hours +":"+ minutes + ":" + seconds + "  " + day + "-" + month + "-" + year;
        return result;
    }
}
