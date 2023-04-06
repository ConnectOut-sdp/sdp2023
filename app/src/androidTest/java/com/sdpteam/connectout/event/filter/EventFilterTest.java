package com.sdpteam.connectout.event.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.nearbyEvents.filter.EventFilter;

import org.junit.Test;

public class EventFilterTest {

    @Test
    public void blankFilterShouldAlwaysPass() {
        assertTrue(EventFilter.NONE.test(new Event("id", "title", "description", null, "me")));
        assertTrue(EventFilter.NONE.test(null));
    }

    @Test
    public void testCombinationOfFilters() {
        final EventFilter t = e -> true;
        final EventFilter f = e -> false;
        assertFalse(t.and(f).test(null));
        assertFalse(f.and(t).test(null));
        assertFalse(f.and(f).test(null));
        assertTrue(t.and(t).test(null));
    }
}
