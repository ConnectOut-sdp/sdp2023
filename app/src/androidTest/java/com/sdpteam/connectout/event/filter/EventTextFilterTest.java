package com.sdpteam.connectout.event.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.Test;

import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.nearbyEvents.filter.EventTextFilter;

public class EventTextFilterTest {

    @Test
    public void filterWithTitle() {
        assertTrue(new EventTextFilter("football").test(new Event("id", "Football in the sand", "description", null, "me")));
        assertTrue(new EventTextFilter("ball").test(new Event("id", "Football in the sand", "description", null, "me")));
        assertTrue(new EventTextFilter("FOOT").test(new Event("id", "Football in the sand", "description", null, "me")));
        assertFalse(new EventTextFilter("judo").test(new Event("id", "Football in the sand", "description", null, "me")));
    }

    @Test
    public void filterWithDescription() {
        assertTrue(new EventTextFilter("football").test(new Event("id", "title", "Football in the sand", null, "me")));
        assertTrue(new EventTextFilter("ball").test(new Event("id", "title", "Football in the sand", null, "me")));
        assertTrue(new EventTextFilter("FOOT").test(new Event("id", "title", "Football in the sand", null, "me")));
        assertFalse(new EventTextFilter("judo").test(new Event("id", "Football in the sand", "description", null, "me")));
    }
}
