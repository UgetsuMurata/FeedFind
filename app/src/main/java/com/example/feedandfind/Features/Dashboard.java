package com.example.feedandfind.Features;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.drawerlayout.widget.DrawerLayout;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.MenuItem;
import android.widget.FrameLayout;

import com.example.feedandfind.NavigationMenu.NavigationMenu_DrawerNav;
import com.example.feedandfind.R;
import com.google.android.material.navigation.NavigationView;


public class Dashboard extends NavigationMenu_DrawerNav {

    DrawerLayout drawerLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.features_dashboard);
    }
}