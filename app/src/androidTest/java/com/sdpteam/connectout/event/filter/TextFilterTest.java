package com.sdpteam.connectout.event.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.nearbyEvents.filter.TextFilter;

import org.junit.Test;

public class TextFilterTest {

    @Test
    public void filterWithTitle() {
        assertTrue(new TextFilter("football").test(new Event("id", "Football in the sand", "description", null, "me")));
        assertTrue(new TextFilter("ball").test(new Event("id", "Football in the sand", "description", null, "me")));
        assertTrue(new TextFilter("FOOT").test(new Event("id", "Football in the sand", "description", null, "me")));
        assertFalse(new TextFilter("judo").test(new Event("id", "Football in the sand", "description", null, "me")));
    }

    @Test
    public void filterWithDescription() {
        assertTrue(new TextFilter("football").test(new Event("id", "title", "Football in the sand", null, "me")));
        assertTrue(new TextFilter("ball").test(new Event("id", "title", "Football in the sand", null, "me")));
        assertTrue(new TextFilter("FOOT").test(new Event("id", "title", "Football in the sand", null, "me")));
        assertFalse(new TextFilter("judo").test(new Event("id", "Football in the sand", "description", null, "me")));
    }

}
