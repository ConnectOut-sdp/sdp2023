package com.sdpteam.connectout.event.nearbyEvents.map;

import static java.lang.Math.atan2;
import static java.lang.Math.cos;
import static java.lang.Math.sin;
import static java.lang.Math.sqrt;
import static java.lang.Math.toRadians;

import com.google.android.gms.maps.model.LatLng;

public class GPSCoordinates {
    private final double latitude;
    private final double longitude;
    private static final int EARTH_RADIUS = 6371;

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

    public double distanceTo(GPSCoordinates other) {
        // Haversine formula

        final double dLat = toRadians(latitude - other.latitude);
        final double dLon = toRadians(longitude - other.longitude);

        final double a = sin(dLat / 2) * sin(dLat / 2) + cos(toRadians(latitude)) * cos(toRadians(other.latitude)) * sin(dLon / 2) * sin(dLon / 2);
        final double c = 2 * atan2(sqrt(a), sqrt(1 - a));
        return c * EARTH_RADIUS;
    }

    public static GPSCoordinates current() {
        // TODO request real GPS position from device
        return new GPSCoordinates(46.51838780003427, 6.568214912591986);
    }
}
