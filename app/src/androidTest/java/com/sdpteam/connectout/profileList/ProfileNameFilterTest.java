package com.sdpteam.connectout.profileList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileEntry;
import com.sdpteam.connectout.profileList.filter.ProfileNameFilter;

import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class ProfileNameFilterTest {

    @Test
    public void testNameFilterMatches() {
        final ProfileNameFilter filter1 = new ProfileNameFilter("ron");
        final ProfileNameFilter filter2 = new ProfileNameFilter("RON");
        final ProfileNameFilter filter3 = new ProfileNameFilter("Ronaldo");
        final ProfileNameFilter filter4 = new ProfileNameFilter("r   ");

        final Profile profile = new Profile("", "Ronaldo", "", "", Profile.Gender.MALE, 0, 0, "");
        final ProfileEntry entry = new ProfileEntry(profile, new ArrayList<>());

        assertTrue(filter1.test(entry));
        assertTrue(filter2.test(entry));
        assertTrue(filter3.test(entry));
        assertTrue(filter4.test(entry));
    }

    @Test
    public void testNameFilterMismatches() {
        final ProfileNameFilter filter = new ProfileNameFilter("x");

        final Profile profile = new Profile("", "Ronaldo", "", "", Profile.Gender.MALE, 0, 0, "");
        final ProfileEntry entry = new ProfileEntry(profile, new ArrayList<>());

        assertFalse(filter.test(entry));
    }
}