package com.sdpteam.connectout.event.location;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import java.util.Objects;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.location.Location;
import androidx.core.app.ActivityCompat;

public class LocationHelper {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static LocationHelper INSTANCE;
    private final FusedLocationProviderClient fusedLocationClient;
    private Location lastLocation;

    private LocationHelper(Context context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    /**
     * Returns a singleton instance of the {@link LocationHelper} class.
     * If the instance has not been created yet, a new instance will be created.
     *
     * @param context The context used to create the instance
     * @return The singleton instance of the {@link LocationHelper} class
     */
    public static synchronized LocationHelper getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LocationHelper(context.getApplicationContext());
        }
        return INSTANCE;
    }

    public static LatLng toLatLng(Location location) {
        Objects.requireNonNull(location);
        return new LatLng(location.getLatitude(), location.getLongitude());
    }

    /**
     * Gets the last known location of the device.
     * If the location is already available, the provided {@link LocationCallback} will be called immediately
     * with the last known location. Otherwise, the method will request location permission and retrieve the
     * last known location if permission is granted. Otherwise it will request permission to the user.
     *
     * @param activity The activity used to request location permission (needed for checking permission)
     * @param callback The callback to receive the location result
     */
    @SuppressLint("MissingPermission")
    public void getLastLocation(Activity activity, LocationCallback callback) {
        if (lastLocation != null) {
            callback.onResult(lastLocation);
            return;
        }

        if (hasPermission(activity)) {
            fusedLocationClient.getLastLocation()
                    .addOnSuccessListener(location -> {
                        lastLocation = location;
                        callback.onResult(location);
                    })
                    .addOnFailureListener(callback::onError);
        } else {
            ActivityCompat.requestPermissions(activity, new String[]{ACCESS_FINE_LOCATION}, LOCATION_PERMISSION_REQUEST_CODE);
        }
    }

    public boolean hasPermission(Activity activity) {
        return ActivityCompat.checkSelfPermission(activity, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED;
    }

    public interface LocationCallback {

        void onResult(Location location);

        default void onError(Exception e) {
            e.printStackTrace();
            onResult(null);
        }
    }
}
