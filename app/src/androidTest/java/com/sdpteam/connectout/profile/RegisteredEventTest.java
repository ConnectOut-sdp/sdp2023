package com.sdpteam.connectout.profile;

import static com.sdpteam.connectout.profile.RegisteredEvent.NULL_REGISTERED_EVENT;
import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RegisteredEventTest {

    @Test
    public void testGetEventDate() {
        final RegisteredEvent registeredEvent = new RegisteredEvent(1, "id", "title");
        assertEquals(1, registeredEvent.getEventDate());
    }

    @Test
    public void testGetEventId() {
        final RegisteredEvent registeredEvent = new RegisteredEvent(1, "id", "title");
        assertEquals("id", registeredEvent.getEventId());
    }

    @Test
    public void testGetEventTitle() {
        final RegisteredEvent registeredEvent = new RegisteredEvent(1, "id", "title");
        assertEquals("title", registeredEvent.getEventTitle());
    }

    @Test
    public void testNullRegisteredEvent() {
        assertEquals(0, NULL_REGISTERED_EVENT.getEventDate());
        assertEquals("", NULL_REGISTERED_EVENT.getEventId());
        assertEquals("", NULL_REGISTERED_EVENT.getEventTitle());
    }
}
