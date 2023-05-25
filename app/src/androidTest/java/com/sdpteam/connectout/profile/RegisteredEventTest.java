package com.sdpteam.connectout.profile;

import static com.sdpteam.connectout.profile.RegisteredEvent.NULL_REGISTERED_EVENT;
import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class RegisteredEventTest {

    @Test
    public void testGetEventId() {
        final RegisteredEvent registeredEvent = new RegisteredEvent("id");
        assertEquals("id", registeredEvent.getEventId());
    }

    @Test
    public void testNullRegisteredEvent() {
        assertEquals("null_user", NULL_REGISTERED_EVENT.getEventId());
    }
}
