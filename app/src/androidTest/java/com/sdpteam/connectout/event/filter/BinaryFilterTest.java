package com.sdpteam.connectout.event.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.nearbyEvents.filter.BinaryFilter;
import com.sdpteam.connectout.event.nearbyEvents.filter.EventFilter;
import com.sdpteam.connectout.event.nearbyEvents.filter.ParticipantRatingFilter;
import com.sdpteam.connectout.event.nearbyEvents.filter.ParticipantsFilter;
import com.sdpteam.connectout.event.nearbyEvents.filter.TextFilter;
import com.sdpteam.connectout.event.nearbyEvents.map.GPSCoordinates;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class BinaryFilterTest {

    private final static Event BASKETBALL_EVENT = new Event("Basketball event", "Basketball", "A basketball game",
            new GPSCoordinates(0, 0), "John");

    private final static Event FOOTBALL_EVENT = new Event("Football event", "Football", "A football game",
            new GPSCoordinates(0, 0), "Jane");

    @Before
    public void setUp() {
        BASKETBALL_EVENT.addParticipant("john123");
        BASKETBALL_EVENT.addParticipant("jane456");
        BASKETBALL_EVENT.addParticipant("bob789");

        FOOTBALL_EVENT.addParticipant("jane456");
        FOOTBALL_EVENT.addParticipant("susan321");
    }

    @Test
    public void testAnd() {
        EventFilter eventFilter = new TextFilter("Basketball");
        ParticipantsFilter participantsFilter = new ParticipantRatingFilter(3.0);
        BinaryFilter filter = new BinaryFilter(eventFilter).and(eventFilter, participantsFilter);

        assertTrue(filter.testEvent(BASKETBALL_EVENT));
        assertFalse(filter.testEvent(FOOTBALL_EVENT));
    }

    @Test
    public void testOr() {
        EventFilter eventFilter = new TextFilter("Basketball");
        ParticipantsFilter participantsFilter = new ParticipantRatingFilter(3.0);
        BinaryFilter filter = new BinaryFilter().or(eventFilter, participantsFilter);

        assertTrue(filter.testEvent(BASKETBALL_EVENT));
        assertTrue(filter.testEvent(FOOTBALL_EVENT));
    }

    @Test
    public void testNegate() {
        EventFilter eventFilter = new TextFilter("Basketball");
        ParticipantsFilter participantsFilter = new ParticipantRatingFilter(3.0);
        BinaryFilter filter = new BinaryFilter(eventFilter,participantsFilter).and(eventFilter, ParticipantsFilter.NONE).negate();

        assertFalse(filter.testEvent(BASKETBALL_EVENT));
        assertTrue(filter.testEvent(FOOTBALL_EVENT));
    }
}
