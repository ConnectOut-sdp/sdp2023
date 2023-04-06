package com.sdpteam.connectout.event.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sdpteam.connectout.event.nearbyEvents.filter.ParticipantsFilter;
import com.sdpteam.connectout.profile.Profile;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ParticipantsFilterTest {

    @Test
    public void blankFilterShouldAlwaysPass() {
        List<Profile> profiles = new ArrayList<>();
        profiles.add(new Profile("id", "name", "email", "bio", Profile.Gender.MALE, 5, 10));
        assertTrue(ParticipantsFilter.NONE.test(profiles));
        assertTrue(ParticipantsFilter.NONE.test(null));
    }

    @Test
    public void testCombinationOfFilters() {
        final ParticipantsFilter t = e -> true;
        final ParticipantsFilter f = e -> false;
        assertFalse(t.and(f).test(null));
        assertFalse(f.and(t).test(null));
        assertFalse(f.and(f).test(null));
        assertTrue(t.and(t).test(null));
    }
}
