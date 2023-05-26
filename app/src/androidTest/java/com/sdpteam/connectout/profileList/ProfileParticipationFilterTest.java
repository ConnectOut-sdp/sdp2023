package com.sdpteam.connectout.profileList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileEntry;
import com.sdpteam.connectout.profile.RegisteredEvent;
import com.sdpteam.connectout.profileList.filter.ProfileParticipationFilter;

import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class ProfileParticipationFilterTest {

    @Test
    public void testParticipationFilterMatches() {
        final ProfileParticipationFilter filter1 = new ProfileParticipationFilter("1");
        final ProfileParticipationFilter filter2 = new ProfileParticipationFilter("2");
        final ProfileParticipationFilter filter3 = new ProfileParticipationFilter("3");

        final List<RegisteredEvent> events = new ArrayList<>();
        events.add(new RegisteredEvent("1"));
        events.add(new RegisteredEvent("2"));
        events.add(new RegisteredEvent("3"));

        final ProfileEntry entry = new ProfileEntry(Profile.NULL_PROFILE, events);

        assertTrue(filter1.test(entry));
        assertTrue(filter2.test(entry));
        assertTrue(filter3.test(entry));
    }

    @Test
    public void testParticipationFilterMismatches() {
        final ProfileParticipationFilter filter = new ProfileParticipationFilter("x");

        final List<RegisteredEvent> events = new ArrayList<>();
        events.add(new RegisteredEvent("1"));
        events.add(new RegisteredEvent("2"));
        events.add(new RegisteredEvent("3"));

        final ProfileEntry entry = new ProfileEntry(Profile.NULL_PROFILE, events);

        assertFalse(filter.test(entry));
    }
}
