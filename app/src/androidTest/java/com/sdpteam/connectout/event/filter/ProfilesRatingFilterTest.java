package com.sdpteam.connectout.event.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sdpteam.connectout.event.nearbyEvents.filter.ProfilesRatingFilter;
import com.sdpteam.connectout.profile.Profile;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProfilesRatingFilterTest {

    private static List<Profile> createGreaterList() {
        return Arrays.asList(
                new Profile("John", "Doe", "mail", "bio", Profile.Gender.MALE, 4.5, 10, ""),
                new Profile("Jane", "Doe", "mail", "bio", Profile.Gender.FEMALE, 3.0, 10, ""),
                new Profile("Bob", "Doe", "mail", "bio", Profile.Gender.OTHER, 3.8, 10, "")
        );
    }

    private static List<Profile> createMixList() {
        return Arrays.asList(
                new Profile("John", "Doe", "mail", "bio", Profile.Gender.MALE, 4.5, 10, ""),
                new Profile("Jane", "Doe", "mail", "bio", Profile.Gender.FEMALE, 2.0, 10, ""),
                new Profile("Bob", "Doe", "mail", "bio", Profile.Gender.OTHER, 2.76, 10, "")
        );
    }

    private static List<Profile> createSmallerList() {
        return Arrays.asList(
                new Profile("John", "Doe", "mail", "bio", Profile.Gender.MALE, 1.5, 10, ""),
                new Profile("Jane", "Doe", "mail", "bio", Profile.Gender.FEMALE, 2.0, 10, ""),
                new Profile("Bob", "Doe", "mail", "bio", Profile.Gender.OTHER, 2.76, 10, "")
        );
    }

    @Test
    public void testAllProfilesWithRatingEqualOrGreater() {
        ProfilesRatingFilter filter = new ProfilesRatingFilter(3.0);
        assertTrue(filter.test(createGreaterList()));
    }

    @Test
    public void testSomeProfilesWithRatingLessThan() {
        ProfilesRatingFilter filter = new ProfilesRatingFilter(3.0);
        assertFalse(filter.test(createMixList()));
    }

    @Test
    public void testAllProfilesWithRatingLessThan() {
        ProfilesRatingFilter filter = new ProfilesRatingFilter(3.0);
        assertFalse(filter.test(createSmallerList()));
    }

    @Test
    public void testEmptyProfileList() {
        ProfilesRatingFilter filter = new ProfilesRatingFilter(3.0);
        assertTrue(filter.test(new ArrayList<>()));
    }

    @Test
    public void testNullValue() {
        ProfilesRatingFilter filter = new ProfilesRatingFilter(null);
        assertTrue(filter.test(createGreaterList()));
    }
}
