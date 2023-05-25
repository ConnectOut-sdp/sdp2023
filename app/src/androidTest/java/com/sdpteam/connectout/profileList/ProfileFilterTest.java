package com.sdpteam.connectout.profileList;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileEntry;
import com.sdpteam.connectout.profileList.filter.ProfileFilter;

import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class ProfileFilterTest {

    @Test
    public void testCombineFiltersWithAnd() {
        final ProfileFilter f1 = e -> true;
        final ProfileFilter f2 = e -> false;

        assertTrue(f1.and(f1).test(null));
        assertFalse(f1.and(f2).test(null));
        assertFalse(f2.and(f1).test(null));
        assertFalse(f2.and(f2).test(null));
    }

    @Test
    public void testNoneFilterAlwaysMatches() {
        assertTrue(ProfileFilter.NONE.test(null));
        assertTrue(ProfileFilter.NONE.test(new ProfileEntry(Profile.NULL_PROFILE, new ArrayList<>())));
    }
}
