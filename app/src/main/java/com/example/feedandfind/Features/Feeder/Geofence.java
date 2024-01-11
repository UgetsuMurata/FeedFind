package com.example.feedandfind.Features.Feeder;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.feedandfind.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Geofence extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.features_feeder_geofence);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.Feeder_Configuration_General_Geofence);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.Feeder_Configuration_General:
                    startActivity(new Intent(getApplicationContext(), General.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
                case R.id.Feeder_Configuration_General_Geofence:
                    return true;

                case R.id.Feeder_Configuration_General_Logs:
                    startActivity(new Intent(getApplicationContext(), Logs.class));
                    overridePendingTransition(0, 0);
                    finish();
                    return true;
            }
            return false;
        });
    }
}