package com.example.feedandfind.Items;

import com.google.android.gms.maps.model.LatLng;

public class Pet {
    String QR;
    LatLngTime currentLocation;
    PedometerData currentPedometerData;

    public Pet(String QR, LatLngTime currentLocation, PedometerData currentPedometerData) {
        this.QR = QR;
        this.currentLocation = currentLocation;
        this.currentPedometerData = currentPedometerData;
    }

    public String getQR() {
        return QR;
    }

    public void setQR(String QR) {
        this.QR = QR;
    }

    public LatLngTime getCurrentLocation() {
        return currentLocation;
    }

    public void setCurrentLocation(LatLngTime currentLocation) {
        this.currentLocation = currentLocation;
    }

    public PedometerData getCurrentPedometerData() {
        return currentPedometerData;
    }

    public void setCurrentPedometerData(PedometerData currentPedometerData) {
        this.currentPedometerData = currentPedometerData;
    }
}
