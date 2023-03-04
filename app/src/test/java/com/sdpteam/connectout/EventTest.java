package com.sdpteam.connectout;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import com.sdpteam.connectout.map.Event;

public class EventTest {
    @Test
    public void eventTest() {
        Event e1 = new Event("event1", 0.1, 0.2);
        assertThat(e1.getLat(), is(0.1));
        assertThat(e1.getLng(), is(0.2));
        assertThat(e1.getTitle(), is("event1"));
    }
}
