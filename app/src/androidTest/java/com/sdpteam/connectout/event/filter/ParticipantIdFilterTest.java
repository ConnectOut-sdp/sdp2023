package com.sdpteam.connectout.event.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.nearbyEvents.filter.ParticipantIdFilter;
import com.sdpteam.connectout.event.nearbyEvents.map.GPSCoordinates;

import org.junit.Test;

public class ParticipantIdFilterTest {
    private static final GPSCoordinates EPFL = new GPSCoordinates(46.51883096217942, 6.566407414078399);

    private final static Event TEST_EVENT1 = new Event("1", "event1", "descr", EPFL, "Bob");
    private final static Event TEST_EVENT2 = new Event("2", "event1", "descr", EPFL, "Alice");

    @Test
    public void filterPassWithOrganizerId() {
        final ParticipantIdFilter filter = new ParticipantIdFilter("Bob");
        assertTrue(filter.test(TEST_EVENT1));
    }

    @Test
    public void filterPassWithParticipantId() {
        final String id = "123";
        TEST_EVENT1.addParticipant(id);
        final ParticipantIdFilter filter = new ParticipantIdFilter(id);
        assertTrue(filter.test(TEST_EVENT1));
    }

    @Test
    public void filterPassFailWithWrongId() {
        final String id = "123";
        final ParticipantIdFilter filter = new ParticipantIdFilter(id);
        assertFalse(filter.test(TEST_EVENT2));
    }
}
