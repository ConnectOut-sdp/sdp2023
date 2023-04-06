package com.sdpteam.connectout.event.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sdpteam.connectout.event.nearbyEvents.filter.ParticipantRatingFilter;
import com.sdpteam.connectout.profile.Profile;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ParticipantRatingFilterTest {

    @Test
    void testAllProfilesWithRatingEqualOrGreater() {
        ParticipantRatingFilter filter = new ParticipantRatingFilter(3.0);
        assertTrue(filter.test(createGreaterList()));
    }

    @Test
    void testSomeProfilesWithRatingLessThan() {
        ParticipantRatingFilter filter = new ParticipantRatingFilter(3.0);
        assertFalse(filter.test(createMixList()));
    }

    @Test
    void testAllProfilesWithRatingLessThan() {
        ParticipantRatingFilter filter = new ParticipantRatingFilter(3.0);
        assertFalse(filter.test(createSmallerList()));
    }

    @Test
    void testEmptyProfileList() {
        ParticipantRatingFilter filter = new ParticipantRatingFilter(3.0);
        assertTrue(filter.test(new ArrayList<>()));
    }

    @Test
    void testNullValue() {
        ParticipantRatingFilter filter = new ParticipantRatingFilter(null);
        assertTrue(filter.test(createGreaterList()));
    }

    private static List<Profile> createGreaterList() {
        return Arrays.asList(
                new Profile("John", "Doe", "mail", "bio", Profile.Gender.MALE, 4.5, 10),
                new Profile("Jane", "Doe", "mail", "bio", Profile.Gender.FEMALE, 3.0, 10),
                new Profile("Bob", "Doe", "mail", "bio", Profile.Gender.OTHER, 3.8, 10)
        );
    }

    private static List<Profile> createMixList() {
        return Arrays.asList(
                new Profile("John", "Doe", "mail", "bio", Profile.Gender.MALE, 4.5, 10),
                new Profile("Jane", "Doe", "mail", "bio", Profile.Gender.FEMALE, 2.0, 10),
                new Profile("Bob", "Doe", "mail", "bio", Profile.Gender.OTHER, 2.76, 10)
        );

    }

    private static List<Profile> createSmallerList() {
        return Arrays.asList(
                new Profile("John", "Doe", "mail", "bio", Profile.Gender.MALE, 1.5, 10),
                new Profile("Jane", "Doe", "mail", "bio", Profile.Gender.FEMALE, 2.0, 10),
                new Profile("Bob", "Doe", "mail", "bio", Profile.Gender.OTHER, 2.76, 10)
        );

    }
}