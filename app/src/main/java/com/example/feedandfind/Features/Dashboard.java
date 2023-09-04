package com.example.feedandfind.Features;

import android.os.Bundle;

import com.example.feedandfind.NavigationMenu.NavigationDrawer;
import com.example.feedandfind.R;
import com.example.feedandfind.databinding.FeaturesDashboardBinding;


public class Dashboard extends NavigationDrawer {

    FeaturesDashboardBinding activityNavigationDrawerBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activityNavigationDrawerBinding = FeaturesDashboardBinding.inflate(getLayoutInflater());
        setContentView(activityNavigationDrawerBinding.getRoot());
    }
}