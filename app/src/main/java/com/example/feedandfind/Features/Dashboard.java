package com.example.feedandfind.Features;

import android.os.Bundle;

import com.example.feedandfind.NavigationMenu.NavigationDrawer;
import com.example.feedandfind.R;
import com.example.feedandfind.databinding.ActivityNavigationDrawerBinding;


public class Dashboard extends NavigationDrawer {

    ActivityNavigationDrawerBinding activityNavigationDrawerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityNavigationDrawerBinding = ActivityNavigationDrawerBinding.inflate(getLayoutInflater());
        setContentView(activityNavigationDrawerBinding.getRoot());
    }
}