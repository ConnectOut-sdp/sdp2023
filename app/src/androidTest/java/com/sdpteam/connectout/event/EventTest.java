package com.sdpteam.connectout.event;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import com.google.android.gms.maps.model.LatLng;
import com.sdpteam.connectout.map.GPSCoordinates;

import org.junit.Test;

public class EventTest {

    private final static GPSCoordinates TEST_COORDINATES = new GPSCoordinates(new LatLng(0.1, 0.2));
    private final static Event TEST_EVENT = new Event("event1", TEST_COORDINATES, "descr", "Bob", "3");

    @Test
    public void testConstructorWithTitle() {
        assertThat(TEST_EVENT.getTitle(), is("event1"));
    }

    @Test
    public void testConstructorWithCoordinates() {
        assertThat(TEST_EVENT.getGpsCoordinates().getLatitude(), is(0.1));
        assertThat(TEST_EVENT.getGpsCoordinates().getLongitude(), is(0.2));
    }

    @Test
    public void testConstructorWithDescription() {
        assertThat(TEST_EVENT.getDescription(), is("descr"));
    }

}