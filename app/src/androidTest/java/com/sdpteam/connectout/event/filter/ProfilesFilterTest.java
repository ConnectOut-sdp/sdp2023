package com.sdpteam.connectout.event.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sdpteam.connectout.event.nearbyEvents.filter.ProfilesFilter;
import com.sdpteam.connectout.profile.Profile;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class ProfilesFilterTest {

    @Test
    public void blankFilterShouldAlwaysPass() {
        List<Profile> profiles = new ArrayList<>();
        profiles.add(new Profile("id", "name", "email", "bio", Profile.Gender.MALE, 5, 10));
        assertTrue(ProfilesFilter.NONE.test(profiles));
        assertTrue(ProfilesFilter.NONE.test(null));
    }

    @Test
    public void testAndCombinationOfFilters() {
        final ProfilesFilter t = e -> true;
        final ProfilesFilter f = e -> false;
        assertFalse(t.and(f).test(null));
        assertFalse(f.and(t).test(null));
        assertFalse(f.and(f).test(null));
        assertTrue(t.and(t).test(null));
    }
    @Test
    public void testOrCombinationOfFilters() {
        final ProfilesFilter t = e -> true;
        final ProfilesFilter f = e -> false;
        assertTrue(t.or(f).test(null));
        assertTrue(f.or(t).test(null));
        assertFalse(f.or(f).test(null));
        assertTrue(t.or(t).test(null));
    }
    @Test
    public void testNegateCombinationOfFilters() {
        final ProfilesFilter t = e -> true;
        final ProfilesFilter f = e -> false;
        assertFalse(t.negate().test(null));
        assertTrue(f.negate().test(null));
    }

}
