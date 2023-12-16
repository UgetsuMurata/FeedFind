package com.example.feedandfind.Features.Finder;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.content.res.ColorStateList;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.example.feedandfind.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.LatLng;

import java.util.concurrent.CountDownLatch;

public class GPSMap extends AppCompatActivity {

    private static final int PERMISSION_REQUEST_LOCATION = 1;
    LatLng viewLatLng;

    MapView mapView;
    GoogleMap googleMap;

    //Layouts for changing geofence
    LinearLayout GeofenceSettings, CancelSaveButtons;
    ImageView decreaseRadius, increaseRadius;
    TextView radius;
    Integer geofenceRadius = 5, storedGeofenceRadius = 5;
    CardView cancelGeofence, saveGeofence;

    private enum Page{
        editGeofence, gpsMap
    }
    Page page = Page.gpsMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.features_gpsmap);

        GeofenceSettings = findViewById(R.id.geofence_settings);
        CancelSaveButtons = findViewById(R.id.cancel_save_buttons);
        decreaseRadius = findViewById(R.id.decrease_radius);
        increaseRadius = findViewById(R.id.increase_radius);
        radius = findViewById(R.id.radius);
        cancelGeofence = findViewById(R.id.cancel_geofence);
        saveGeofence = findViewById(R.id.save_geofence);

        radius.setText(String.valueOf(geofenceRadius));

        findViewById(R.id.open_menu).setOnClickListener(v -> {
            PopupMenu popup = new PopupMenu(GPSMap.this, v);
            MenuInflater inflater = popup.getMenuInflater();
            inflater.inflate(R.menu.bottom_menu, popup.getMenu());
            popup.show();
            popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                @Override
                public boolean onMenuItemClick(MenuItem menuItem) {
//                    if (menuItem.getItemId() == R.id.edit_geofence){
//                        showEdit();
//                        return true;
//                    }
                    return false;
                }
            });
        });

        cancelGeofence.setOnClickListener(view -> showGPSMap());
        saveGeofence.setOnClickListener(view -> saveAndReinitializeGeofence());
        Handler handler = new Handler();
        Runnable runnableIncrease = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 100);
                increaseRadius();
            }
        };
        Runnable runnableDecrease = new Runnable() {
            @Override
            public void run() {
                handler.postDelayed(this, 100);
                decreaseRadius();
            }
        };
        decreaseRadius.setOnClickListener(view -> {
            decreaseRadius();
            handler.removeCallbacks(runnableDecrease);
        });
        increaseRadius.setOnClickListener(view -> {
            increaseRadius();
            handler.removeCallbacks(runnableIncrease);
        });
        decreaseRadius.setOnLongClickListener(view -> {
            handler.postDelayed(runnableDecrease, 0);
            return true;
        });
        increaseRadius.setOnLongClickListener(view -> {
            handler.postDelayed(runnableIncrease, 0);
            return true;
        });
        decreaseRadius.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                handler.removeCallbacks(runnableDecrease);
            }
            return false;
        });
        increaseRadius.setOnTouchListener((view, motionEvent) -> {
            if (motionEvent.getAction() == MotionEvent.ACTION_UP) {
                handler.removeCallbacks(runnableIncrease);
            }
            return false;
        });
        if (mapView == null) return;
        mapView.onCreate(savedInstanceState);
        mapView.onResume();
        mapView.getMapAsync(new OnMapReadyCallback() {
            @Override
            public void onMapReady(@NonNull GoogleMap googleMap) {
                GPSMap.this.googleMap = googleMap;
                GPSMap.this.googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);

                if (ContextCompat.checkSelfPermission(GPSMap.this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                        && ContextCompat.checkSelfPermission(GPSMap.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                    // Request permissions if not granted
                    ActivityCompat.requestPermissions(GPSMap.this,
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                            PERMISSION_REQUEST_LOCATION);
                } else {
                    viewLatLng = getPhoneLocation();
                    if (viewLatLng!=null) onSetUserView(viewLatLng);
                }
            }
        });
    }

    private void increaseRadius(){
        if (geofenceRadius < 100){
            geofenceRadius++;
            radius.setText(String.valueOf(geofenceRadius));
            if (geofenceRadius == 2) {
                TypedValue typedValue = new TypedValue();
                GPSMap.this.getTheme().resolveAttribute(R.attr.text, typedValue, true);
                decreaseRadius.setImageTintList(ColorStateList.valueOf(typedValue.data));
            }
        }
        if (geofenceRadius == 100){
            increaseRadius.setImageTintList(ContextCompat.getColorStateList(GPSMap.this, R.color.grey));
        }
    }
    private void decreaseRadius(){
        if (geofenceRadius > 1){
            geofenceRadius--;
            radius.setText(String.valueOf(geofenceRadius));
            if (geofenceRadius == 99) {
                TypedValue typedValue = new TypedValue();
                GPSMap.this.getTheme().resolveAttribute(R.attr.text, typedValue, true);
                increaseRadius.setImageTintList(ContextCompat.getColorStateList(GPSMap.this, typedValue.data));
            }
        }
        if (geofenceRadius == 1){
            decreaseRadius.setImageTintList(ContextCompat.getColorStateList(GPSMap.this, R.color.grey));
        }
    }

    private void saveAndReinitializeGeofence(){
        storedGeofenceRadius = geofenceRadius;
        //store new value
        showGPSMap();
    }

    private void showEdit(){
        GeofenceSettings.setVisibility(View.VISIBLE);
        CancelSaveButtons.setVisibility(View.VISIBLE);
        page = Page.editGeofence;
    }
    private void showGPSMap(){
        GeofenceSettings.setVisibility(View.GONE);
        CancelSaveButtons.setVisibility(View.GONE);
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == PERMISSION_REQUEST_LOCATION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                viewLatLng = getPhoneLocation();
                if (viewLatLng!=null) onSetUserView(viewLatLng);
            }
        }
    }

    private void onSetUserView(LatLng latLng){
        googleMap.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 20));
    }
    @SuppressLint("MissingPermission")
    @Nullable
    private LatLng getPhoneLocation() {
        LocationManager locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if (locationManager != null && locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
            Location lastKnownLocation = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);

            final double[] latitude = new double[1];
            final double[] longitude = new double[1];
            final CountDownLatch latch = new CountDownLatch(1);

            if (lastKnownLocation != null) {
                latitude[0] = lastKnownLocation.getLatitude();
                longitude[0] = lastKnownLocation.getLongitude();
                Log.d("GPS", "I GOT HERE: "+latitude[0]+", "+longitude[0]);
                return new LatLng(latitude[0], longitude[0]);
            } else {
                locationManager.requestSingleUpdate(LocationManager.GPS_PROVIDER, new LocationListener() {
                    @Override
                    public void onLocationChanged(@NonNull Location location) {
                        latitude[0] = location.getLatitude();
                        longitude[0] = location.getLongitude();
                        latch.countDown();
                    }
                }, null);
                try {
                    latch.await();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                Log.d("GPS", "I GOT HERE 2: "+latitude[0]+", "+longitude[0]);
                return new LatLng(latitude[0], longitude[0]);
            }
        } else {
            Toast.makeText(GPSMap.this, "Please turn on GPS Location.", Toast.LENGTH_SHORT).show();
            return null;
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (mapView != null) {
            mapView.onPause();
        }
    }
    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mapView != null) {
            mapView.onDestroy();
        }
    }
    @Override
    public void onLowMemory() {
        super.onLowMemory();
        if (mapView != null) {
            mapView.onLowMemory();
        }
    }
}