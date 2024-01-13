package com.example.feedandfind.FunctionHelpers;

import androidx.annotation.Nullable;

import java.util.Locale;

public class TimeCode {
    public static String timeCodeToTime(@Nullable String code) {
        // return "N/A" for invalid codes
        if (code == null || "N/A".equals(code) || !code.matches("^[0-9]{4}$")) return "N/A";

        // process valid code
        String hour = code.substring(0, 2);
        String minute = code.substring(2);
        String timePeriod = "AM";
        int actualHour;
        int actualMinute = Integer.parseInt(minute);
        if (Integer.parseInt(hour) > 12) {
            actualHour = Integer.parseInt(hour) - 12;
            timePeriod = "PM";
        } else {
            actualHour = Integer.parseInt(hour);
            actualHour = actualHour == 0 ? 12 : actualHour;
        }

        // return formatted time
        return String.format(Locale.getDefault(), "%02d:%02d%s", actualHour, actualMinute, timePeriod);
    }

    public static String timeToTimeCode(Integer hour, Integer minutes) {
        return String.format(Locale.getDefault(), "%02d%02d", hour, minutes);
    }
}
