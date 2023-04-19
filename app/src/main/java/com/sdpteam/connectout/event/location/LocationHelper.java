package com.sdpteam.connectout.event.location;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.content.pm.PackageManager.PERMISSION_GRANTED;

import android.app.Activity;
import android.content.Context;
import android.location.Location;

import androidx.core.app.ActivityCompat;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;

public class LocationHelper {

    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1;
    private static LocationHelper INSTANCE;
    private final FusedLocationProviderClient fusedLocationClient;
    private Location lastLocation;

    private LocationHelper(Context context) {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(context);
    }

    public static synchronized LocationHelper getInstance(Context context) {
        if (INSTANCE == null) {
            INSTANCE = new LocationHelper(context.getApplicationContext());
        }
        return INSTANCE;
    }

    public void getLastLocation(Activity activity, LocationCallback callback) {
        if (lastLocation != null) {
            callback.onResult(lastLocation);
            return;
        }

        if (ActivityCompat.checkSelfPermission(activity, ACCESS_FINE_LOCATION) == PERMISSION_GRANTED) {
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

    public interface LocationCallback {

        void onResult(Location location);

        default void onError(Exception e) {
            e.printStackTrace();
            onResult(null);
        }
    }

}
