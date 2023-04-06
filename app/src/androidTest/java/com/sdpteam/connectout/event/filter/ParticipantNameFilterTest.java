package com.sdpteam.connectout.event.filter;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import com.sdpteam.connectout.event.nearbyEvents.filter.ParticipantNameFilter;
import com.sdpteam.connectout.profile.Profile;

import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;


public class ParticipantNameFilterTest {

    @Test
    public void testMatchingName() {
        ParticipantNameFilter filter = new ParticipantNameFilter("John");
        assertTrue(filter.test(createListWithMatchingName()));
    }

    @Test
    public void testNonMatchingName() {
        ParticipantNameFilter filter = new ParticipantNameFilter("Mike");
        assertFalse(filter.test(createListWithMatchingName()));
    }

    @Test
    public void testEmptyProfileList() {
        ParticipantNameFilter filter = new ParticipantNameFilter("John");
        assertFalse(filter.test(new ArrayList<>()));
    }

    private static List<Profile> createListWithMatchingName() {
        return Arrays.asList(
                new Profile("John", "Doe", "mail", "bio", Profile.Gender.MALE, 4.5, 10),
                new Profile("Jane", "Doe", "mail", "bio", Profile.Gender.FEMALE, 3.0, 10),
                new Profile("Bob", "Doe", "mail", "bio", Profile.Gender.OTHER, 3.8, 10)
        );
    }
}
