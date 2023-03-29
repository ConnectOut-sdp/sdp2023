package com.sdpteam.connectout.event.nearbyEvents.map;

import com.google.android.gms.maps.model.LatLng;

public class GPSCoordinates {
    private final double latitude;
    private final double longitude;

    private GPSCoordinates() {
        this(0, 0);
    }

    public GPSCoordinates(LatLng position) {
        if (position == null) {
            throw new IllegalArgumentException("coordinates are null");
        }
        latitude = position.latitude;
        longitude = position.longitude;
    }

    public GPSCoordinates(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public LatLng toLatLng() {
        return new LatLng(latitude, longitude);
    }
}
