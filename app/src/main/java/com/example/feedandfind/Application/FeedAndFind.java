package com.example.feedandfind.Application;

import android.app.Application;

import androidx.appcompat.app.AppCompatDelegate;

public class FeedAndFind extends Application {

    FeedAndFind instance;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

        //TODO: Remove this default night mode after debug.
        AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
    }
}
