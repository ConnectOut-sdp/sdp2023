package com.sdpteam.connectout.profileList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileEntry;
import com.sdpteam.connectout.profileList.filter.ProfileRatingFilter;

import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class ProfileRatingFilterTest {

    @Test
    public void testRatingFilterMatches() {
        final ProfileRatingFilter filter1 = new ProfileRatingFilter(0, 5);
        final ProfileRatingFilter filter2 = new ProfileRatingFilter(2, 3);
        final ProfileRatingFilter filter3 = new ProfileRatingFilter(2.5, 2.5);
        final ProfileRatingFilter filter4 = new ProfileRatingFilter(-1, 3);

        final Profile profile = new Profile("", "", "", "", Profile.Gender.MALE, 2.5, 1, "");
        final ProfileEntry entry = new ProfileEntry(profile, new ArrayList<>());

        assertTrue(filter1.test(entry));
        assertTrue(filter2.test(entry));
        assertTrue(filter3.test(entry));
        assertTrue(filter4.test(entry));
    }

    @Test
    public void testRatingFilterMismatches() {
        final ProfileRatingFilter filter1 = new ProfileRatingFilter(3, 2);
        final ProfileRatingFilter filter2 = new ProfileRatingFilter(3, 3);

        final Profile profile = new Profile("", "", "", "", Profile.Gender.MALE, 2.5, 1, "");
        final ProfileEntry entry = new ProfileEntry(profile, new ArrayList<>());

        assertFalse(filter1.test(entry));
        assertFalse(filter2.test(entry));
    }
}
