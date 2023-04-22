package com.sdpteam.connectout.event.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.nearbyEvents.filter.EventLocationFilter;
import com.sdpteam.connectout.event.nearbyEvents.map.GPSCoordinates;

public class EventLocationFilterTest {

    private static final GPSCoordinates EPFL = new GPSCoordinates(46.51883096217942, 6.566407414078399);
    private static final GPSCoordinates OUCHY = new GPSCoordinates(46.51006533094176, 6.627645688633595);
    private static final GPSCoordinates ZURICH = new GPSCoordinates(47.361900382477174, 8.541087892043564);

    @Test
    public void filterShouldPassIfEventIsCloseEnough() {
        final EventLocationFilter filter = new EventLocationFilter(EPFL, 10);
        assertTrue(filter.test(new Event("id", "title", "description", OUCHY, "me")));
        assertFalse(filter.test(new Event("id", "title", "description", ZURICH, "me")));
    }

    @Test
    public void filterShouldNotPassIfOneLocationIsNUll() {
        final EventLocationFilter filter1 = new EventLocationFilter(EPFL, 10);
        final EventLocationFilter filter2 = new EventLocationFilter(null, 10);
        assertFalse(filter1.test(new Event("id", "title", "description", null, "me")));
        assertFalse(filter2.test(new Event("id", "title", "description", EPFL, "me")));
    }

    @Test
    public void filterShouldAlwaysPassIfRadiusIs100() {
        final EventLocationFilter filter = new EventLocationFilter(EPFL, 100);
        assertTrue(filter.test(new Event("id", "title", "description", OUCHY, "me")));
        assertTrue(filter.test(new Event("id", "title", "description", ZURICH, "me")));
    }
}