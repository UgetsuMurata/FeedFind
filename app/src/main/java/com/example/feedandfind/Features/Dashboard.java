package com.example.feedandfind.Features;

import android.os.Bundle;
import android.view.Window;

import com.example.feedandfind.NavigationMenu.NavigationDrawer;
import com.example.feedandfind.R;
import com.example.feedandfind.databinding.ActivityNavigationDrawerBinding;
import com.example.feedandfind.databinding.FeaturesDashboardBinding;


public class Dashboard extends NavigationDrawer {

    FeaturesDashboardBinding featuresDashboardBinding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        featuresDashboardBinding = FeaturesDashboardBinding.inflate(getLayoutInflater());
        setContentView(featuresDashboardBinding.getRoot());
    }



}