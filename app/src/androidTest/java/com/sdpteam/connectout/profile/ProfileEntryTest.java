package com.sdpteam.connectout.profile;

import static org.junit.Assert.assertEquals;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.List;

@RunWith(AndroidJUnit4.class)
public class ProfileEntryTest {

    @Test
    public void testGetProfile() {
        final Profile profile = new Profile("1", "name", "email", "bio", Profile.Gender.MALE, 5, 1, "");
        final ProfileEntry entry = new ProfileEntry(profile, new ArrayList<>());
        assertEquals(profile, entry.getProfile());
    }

    @Test
    public void testGetRegisteredEvents() {
        final List<RegisteredEvent> events = new ArrayList<>();
        events.add(new RegisteredEvent("1"));
        events.add(new RegisteredEvent("2"));
        final ProfileEntry entry = new ProfileEntry(Profile.NULL_PROFILE, events);

        assertEquals(events.size(), entry.getRegisteredEvents().size());
        assertEquals(events.get(0).getEventId(), "1");
        assertEquals(events.get(1).getEventId(), "2");
    }
}