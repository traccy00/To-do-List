package com.example.todolist.common;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.time.LocalTime;
import java.time.temporal.ChronoField;
import java.util.Calendar;

public class Utils {
    @RequiresApi(api = Build.VERSION_CODES.O)
    public long convertStringToLong(String timeString) {
        LocalTime localTime = LocalTime.parse(timeString);
        long timeLong = localTime.getLong(ChronoField.MILLI_OF_SECOND);
        return timeLong;
    }

    public String getHour(String time) {
        String[] s = time.split(":");
        return s[0];
    }

    public String formatDate(Calendar calendar) {
        String date = "";
        return date;
    }

}
