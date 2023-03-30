package com.sdpteam.connectout.event.nearbyEvents.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.nearbyEvents.map.GPSCoordinates;

import org.junit.Test;

public class LocationFilterTest {

    private static final GPSCoordinates EPFL = new GPSCoordinates(46.51883096217942, 6.566407414078399);
    private static final GPSCoordinates OUCHY = new GPSCoordinates(46.51006533094176, 6.627645688633595);
    private static final GPSCoordinates ZURICH = new GPSCoordinates(47.361900382477174, 8.541087892043564);

    @Test
    public void filterShouldPassIfEventIsCloseEnough() {
        final LocationFilter filter = new LocationFilter(EPFL, 10);
        assertTrue(filter.test(new Event("id", "title", "description", OUCHY, "me")));
        assertFalse(filter.test(new Event("id", "title", "description", ZURICH, "me")));
    }

    @Test
    public void filterShouldNotPassIfOneLocationIsNUll() {
        final LocationFilter filter1 = new LocationFilter(EPFL, 10);
        final LocationFilter filter2 = new LocationFilter(null, 10);
        assertFalse(filter1.test(new Event("id", "title", "description", null, "me")));
        assertFalse(filter2.test(new Event("id", "title", "description", EPFL, "me")));
    }

    @Test
    public void filterShouldAlwaysPassIfRadiusIs100() {
        final LocationFilter filter = new LocationFilter(EPFL, 100);
        assertTrue(filter.test(new Event("id", "title", "description", OUCHY, "me")));
        assertTrue(filter.test(new Event("id", "title", "description", ZURICH, "me")));
    }
}