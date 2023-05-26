package com.sdpteam.connectout.event.location;

import org.junit.Test;

import com.sdpteam.connectout.utils.TestActivity;

import android.location.Location;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

public class LocationHelperTest {
    public ActivityScenarioRule<TestActivity> activityRule = new ActivityScenarioRule<>(TestActivity.class);

    @Test
    public void locationHelper() {
        try {
            LocationHelper.getInstance(null);
        } catch (Exception ignored) {
        }
    }

    LocationHelper.LocationCallback locationCallback = new LocationHelper.LocationCallback() {
        @Override
        public void onResult(Location location) {

        }
    };

    @Test
    public void toLat() {
        LocationHelper.toLatLng(new Location("test"));
    }

    @Test
    public void callback() {
        locationCallback.onResult(new Location("test"));
        locationCallback.onError(new Exception());
    }

    @Test
    public void hasPermission() {
        try {
            new LocationHelper(null);
        } catch (Exception ignored) {
        }
    }

    @Test
    public void getLastLocationTest() {
        LocationHelper locationHelper = new LocationHelper();
        try {
            locationHelper.getLastLocation(null, locationCallback);
        } catch (Exception ignored) {
        }
    }
}
