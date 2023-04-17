package com.sdpteam.connectout.event.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sdpteam.connectout.event.nearbyEvents.filter.ProfilesRatingRangeFilter;
import com.sdpteam.connectout.profile.Profile;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProfilesRatingRangeFilterTest {

    @Test
    public void testRatingInRange() {
        ProfilesRatingRangeFilter filter = new ProfilesRatingRangeFilter(2.5, 4.0);
        assertTrue(filter.test(createRatingInRangeList()));
    }

    @Test
    public void testRatingOutOfRange() {
        ProfilesRatingRangeFilter filter = new ProfilesRatingRangeFilter(2.5, 4.0);
        assertFalse(filter.test(createRatingOutOfRangeList()));
    }

    @Test
    public void testEmptyProfileList() {
        ProfilesRatingRangeFilter filter = new ProfilesRatingRangeFilter(2.5, 4.0);
        assertTrue(filter.test(new ArrayList<>()));
    }

    private static List<Profile> createRatingInRangeList() {
        return Arrays.asList(
                new Profile("John", "Doe", "mail", "bio", Profile.Gender.MALE, 4.0, 10, ""),
                new Profile("Jane", "Doe", "mail", "bio", Profile.Gender.FEMALE, 3.0, 10, ""),
                new Profile("Bob", "Doe", "mail", "bio", Profile.Gender.OTHER, 2.5, 10, "")
        );
    }

    private static List<Profile> createRatingOutOfRangeList() {
        return Arrays.asList(
                new Profile("John", "Doe", "mail", "bio", Profile.Gender.MALE, 4.5, 10, ""),
                new Profile("Jane", "Doe", "mail", "bio", Profile.Gender.FEMALE, 1.0, 10, ""),
                new Profile("Bob", "Doe", "mail", "bio", Profile.Gender.OTHER, 2.0, 10, "")
        );
    }
}
