package com.sdpteam.connectout;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class EventTest {
    @Test
    public void eventTest() {
        Event e1 = new Event("event1", 0, 0.2);
        assertThat(e1.getLat(), is(0));
        assertThat(e1.getLng(), is(0.2));
        assertThat(e1.getTitle(), is("event1"));
    }
}
