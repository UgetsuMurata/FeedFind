package com.example.feedandfind.Features;

import android.os.Bundle;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.Switch;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.example.feedandfind.R;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.material.switchmaterial.SwitchMaterial;

public class GPSMap extends AppCompatActivity {

    LinearLayout cornerMenu;
    ConstraintLayout cornerMenuClose, cornerMenuWhole;
    MapView mapView;
    GoogleMap googleMap;
    TextView petOutsideLabel, petOutsideCount;

    ImageView focusGeofence, focusPhone, focusPets;
    SwitchMaterial optionSatelliteView, optionGeofence, optionPetNames;


    //Layouts for changing geofence
    LinearLayout GeofenceSettings, CancelSaveButtons;
    ImageView decreaseRadius, increaseRadius;
    TextView radius;
    CardView cancelGeofence, saveGeofence;

    private enum Page{
        editGeofence, gpsMap
    }
    Page page = Page.gpsMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.features_gpsmap);

        cornerMenu = findViewById(R.id.corner_menu);
        cornerMenuClose = findViewById(R.id.corner_menu_close_button);
        cornerMenuWhole = findViewById(R.id.corner_menu_view);
        mapView = findViewById(R.id.map_view);
        petOutsideLabel = findViewById(R.id.pet_outside_label);
        petOutsideCount = findViewById(R.id.pet_outside_count);
        focusGeofence = findViewById(R.id.focus_geofence);
        focusPhone = findViewById(R.id.focus_phone);
        focusPets = findViewById(R.id.focus_pets);
        optionSatelliteView = findViewById(R.id.option_satellite_view);
        optionGeofence = findViewById(R.id.option_geofence);
        optionPetNames = findViewById(R.id.option_pet_names);

        GeofenceSettings = findViewById(R.id.geofence_settings);
        CancelSaveButtons = findViewById(R.id.cancel_save_buttons);
        decreaseRadius = findViewById(R.id.decrease_radius);
        increaseRadius = findViewById(R.id.increase_radius);
        radius = findViewById(R.id.radius);
        cancelGeofence = findViewById(R.id.cancel_geofence);
        saveGeofence = findViewById(R.id.save_geofence);

        findViewById(R.id.open_menu).setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(GPSMap.this, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.geofence_menu, popup.getMenu());
            popup.show();
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
                    if (menuItem.getItemId() == R.id.edit_geofence){
                        showEdit();
                        return true;
                    }
                    return false;
                }
            });
        });

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

    private void showEdit(){
        GeofenceSettings.setVisibility(View.VISIBLE);
        CancelSaveButtons.setVisibility(View.VISIBLE);
        cornerMenuWhole.setVisibility(View.GONE);
        petOutsideLabel.setVisibility(View.GONE);
        petOutsideCount.setVisibility(View.GONE);
        page = Page.editGeofence;
    }
    private void showGPSMap(){
        GeofenceSettings.setVisibility(View.GONE);
        CancelSaveButtons.setVisibility(View.GONE);
        cornerMenuWhole.setVisibility(View.VISIBLE);
        petOutsideLabel.setVisibility(View.VISIBLE);
        petOutsideCount.setVisibility(View.VISIBLE);
        page = Page.gpsMap;
    }

    @Override
    public void onBackPressed() {
        if (page == Page.gpsMap) {
            super.onBackPressed();
        } else {
            showGPSMap();
        }
    }
}