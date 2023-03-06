package com.sdpteam.connectout.event;

import static org.hamcrest.CoreMatchers.is;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

public class EventTest {

    @Test
    public void constructorInstancedWithGivenValues() {
        Event e1 = new Event("event1", 0.1, 0.2, "descr");
        assertThat(e1.getTitle(), is("event1"));
        assertThat(e1.getLat(), is(0.1));
        assertThat(e1.getLng(), is(0.2));
        assertThat(e1.getDescription(), is("descr"));
    }

    @Test
    public void toStringReturnExpectedString() {
        Event e1 = new Event("event1", 0.1, 0.2, "descr");
        String expectedString = "event1\n is at 0.1 and 0.2";
        assertThat(e1.toString(), is(expectedString));
    }

    @Test
    public void descriptionGetterReturnExpectedString() {
        Event e1 = new Event("event1", 0.1, 0.2, "descr");
        assertThat(e1.getDescription(), is("descr"));
    }

}