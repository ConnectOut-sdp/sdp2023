package com.sdpteam.connectout.event.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Before;
import org.junit.Test;

import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.nearbyEvents.filter.EventDateFilter;
import com.sdpteam.connectout.event.nearbyEvents.map.GPSCoordinates;

import android.icu.util.Calendar;

public class EventDateFilterTest {
    private EventDateFilter filter;
    private Calendar testCalendar;
    private Event eventSameDay;
    private Event eventDifferentDay;

    @Before
    public void setUp() {
        testCalendar = Calendar.getInstance();

        // Event on the same day as the testCalendar
        eventSameDay = new Event(
                "1",
                "TestTitle",
                "TestDescription",
                new GPSCoordinates(0, 0),
                "TestOrganizer",
                null,
                null,
                testCalendar.getTimeInMillis()
        );

        // Event on the next day
        Calendar nextDay = (Calendar) testCalendar.clone();
        nextDay.add(Calendar.DAY_OF_YEAR, 1);
        eventDifferentDay = new Event(
                "2",
                "TestTitle",
                "TestDescription",
                new GPSCoordinates(0, 0),
                "TestOrganizer",
                null,
                null,
                nextDay.getTimeInMillis()
        );
    }

    @Test
    public void testAcceptsSameDay() {
        filter = new EventDateFilter(testCalendar);
        assertTrue(filter.test(eventSameDay));
    }

    @Test
    public void testRejectsDifferentDay() {
        filter = new EventDateFilter(testCalendar);
        assertFalse(filter.test(eventDifferentDay));
    }

    @Test
    public void testAcceptsAllIfNoCalendar() {
        filter = new EventDateFilter(null);
        assertTrue(filter.test(eventSameDay));
        assertTrue(filter.test(eventDifferentDay));
    }
}
