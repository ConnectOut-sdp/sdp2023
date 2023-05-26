package com.sdpteam.connectout.event.location;

import static org.junit.Assert.assertThrows;

import org.junit.Test;

import com.sdpteam.connectout.utils.TestActivity;

import android.location.Location;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

public class LocationHelperTest {
    public ActivityScenarioRule<TestActivity> activityRule = new ActivityScenarioRule<>(TestActivity.class);

    @Test
    public void locationHelper() {
        assertThrows(NullPointerException.class, () -> {
            LocationHelper.getInstance(null);
        });
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
        assertThrows(NullPointerException.class, () -> new LocationHelper(null));
    }

    @Test
    public void getLastLocationTest() {
        LocationHelper locationHelper = new LocationHelper();
        assertThrows(NullPointerException.class, () -> locationHelper.getLastLocation(null, locationCallback));
    }
}
