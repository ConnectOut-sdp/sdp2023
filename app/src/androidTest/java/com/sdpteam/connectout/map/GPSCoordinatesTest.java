package com.sdpteam.connectout.map;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.google.android.gms.maps.model.LatLng;
import com.sdpteam.connectout.event.nearbyEvents.map.GPSCoordinates;

public class GPSCoordinatesTest {
    private final static LatLng VALID_POSITION = new LatLng(37.7749, -122.4194);
    private final static LatLng INVALID_POSITION = null;

    @Test
    public void testConstructorWithValidLatLngPosition() {
        GPSCoordinates coordinates = new GPSCoordinates(VALID_POSITION);
        assertThat(coordinates.getLatitude(), is(VALID_POSITION.latitude));
        assertThat(coordinates.getLongitude(), is(VALID_POSITION.longitude));
    }

    @Test
    public void testConstructorWithTwoCoordinates() {
        GPSCoordinates coordinates = new GPSCoordinates(37.7749, -122.4194);
        assertThat(coordinates.getLatitude(), is(37.7749));
        assertThat(coordinates.getLongitude(), is(-122.4194));
    }

    @Test
    public void testConstructorWithInvalidPosition() {
        assertThrows(IllegalArgumentException.class, () -> new GPSCoordinates(INVALID_POSITION));
    }

    @Test
    public void testGetLatitude() {
        GPSCoordinates coordinates = new GPSCoordinates(VALID_POSITION);
        assertThat(coordinates.getLatitude(), is(VALID_POSITION.latitude));
    }

    @Test
    public void testGetLongitude() {
        GPSCoordinates coordinates = new GPSCoordinates(VALID_POSITION);
        assertThat(coordinates.getLongitude(), is(VALID_POSITION.longitude));
    }

    @Test
    public void testToLatLng() {
        GPSCoordinates coordinates = new GPSCoordinates(VALID_POSITION);
        LatLng latLng = coordinates.toLatLng();
        assertThat(latLng.latitude, is(VALID_POSITION.latitude));
        assertThat(latLng.longitude, is(VALID_POSITION.longitude));
    }

    @Test
    public void testDistanceBetweenTwoCoordinates() {
        final GPSCoordinates epfl = new GPSCoordinates(46.51883096217942, 6.566407414078399);
        final GPSCoordinates ouchy = new GPSCoordinates(46.51006533094176, 6.627645688633595);
        assertTrue(epfl.distanceTo(ouchy) > 2);
        assertTrue(epfl.distanceTo(ouchy) < 5);
    }

    @Test
    public void testDistanceBetweenTwoCoordinatesIsSymmetric() {
        final GPSCoordinates epfl = new GPSCoordinates(46.51883096217942, 6.566407414078399);
        final GPSCoordinates ouchy = new GPSCoordinates(46.51006533094176, 6.627645688633595);
        assertEquals(epfl.distanceTo(ouchy), ouchy.distanceTo(epfl), 0.00000000000001);
    }

    @Test
    public void testDistanceBetweenSamePointsIsZero() {
        final GPSCoordinates coordinates = new GPSCoordinates(46.51883096217942, 6.566407414078399);
        assertEquals(coordinates.distanceTo(coordinates), 0, 0.00000000000001);
    }
}
