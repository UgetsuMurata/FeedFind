package com.example.feedandfind.Items;

import com.google.android.gms.maps.model.LatLng;

import java.util.ArrayList;
import java.util.List;

public class LatLngTime {
    LatLng currentLocation;
    String timestamp;

    public LatLngTime(LatLng currentLocation, String timestamp) throws TimestampException {
        if (!timestamp.matches("^([0-1][0-9]|[2][0-4]):[0-5][0-9]:[0-5][0-9]$")){
            throw new TimestampException("String value does not follow HH:MM:SS format.");
        }
        this.currentLocation = currentLocation;
        this.timestamp = timestamp;
    }

    public LatLng getLatLng(){
        return currentLocation;
    }
    public String getTimestamp24(){
        return timestamp;
    }
    public String getTimestamp12(){
        List<Integer> times = ExtractTimestamp();
        Integer hours = times.get(0);
        Integer mins = times.get(1);
        Integer secs = times.get(2);
        String AMorPM = "AM";
        if (hours > 12){
            AMorPM = "PM";
            hours = hours - 12;
        }
        return hours + ":" + mins + ":" + secs + " " + AMorPM;
    }
    private List<Integer> ExtractTimestamp(){
        String[] rawTimes = timestamp.split(":");
        List<Integer> times = new ArrayList<>();
        for (String rawTime : rawTimes){
            times.add(Integer.valueOf(rawTime));
        }
        return times;
    }
    public class TimestampException extends Exception {
        public TimestampException(String message) {
            super(message);
        }
    }
}
