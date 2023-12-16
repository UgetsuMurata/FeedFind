package com.example.feedandfind.FeederConfiguration;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.example.feedandfind.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class Feeder_Configuration_General_Geofence extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_feeder_configuration_general_geofence);

        BottomNavigationView bottomNavigationView = findViewById(R.id.bottomNavigation);
        bottomNavigationView.setSelectedItemId(R.id.Feeder_Configuration_General_Geofence);
        bottomNavigationView.setOnItemSelectedListener(item -> {
            switch (item.getItemId()) {
                case R.id.Feeder_Configuration_General:
                    startActivity(new Intent(getApplicationContext(), Feeder_Configuration_General.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
                case R.id.Feeder_Configuration_General_Geofence:
                    return true;

                case R.id.Feeder_Configuration_General_Logs:
                    startActivity(new Intent(getApplicationContext(), Feeder_Configuration_General_Logs.class));
                    overridePendingTransition(R.anim.slide_in_right, R.anim.slide_out_left);
                    finish();
                    return true;
            }
            return false;
        });
    }
}