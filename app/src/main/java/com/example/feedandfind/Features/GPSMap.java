package com.example.feedandfind.Features;

import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.feedandfind.R;

public class GPSMap extends AppCompatActivity {

    LinearLayout cornerMenu;
    ConstraintLayout cornerMenuClose;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.features_gpsmap);

        cornerMenu = findViewById(R.id.corner_menu);
        cornerMenuClose = findViewById(R.id.corner_menu_close_button);

        cornerMenu.setVisibility(View.GONE);
        cornerMenuClose.setVisibility(View.GONE);
        findViewById(R.id.corner_menu_button).setOnClickListener(view -> {
            cornerMenu.setVisibility(View.VISIBLE);
            cornerMenuClose.setVisibility(View.VISIBLE);
        });
        cornerMenuClose.setOnClickListener(view -> {
            cornerMenu.setVisibility(View.GONE);
            cornerMenuClose.setVisibility(View.GONE);
        });
    }


}