package com.sdpteam.connectout.profile.editProfile;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.profile.Profile;

import androidx.test.ext.junit.rules.ActivityScenarioRule;

public class EditProfileActivityTest {
    @Rule
    public ActivityScenarioRule<EditProfileActivity> testRule = new ActivityScenarioRule<>(EditProfileActivity.class);

    @BeforeClass
    public static void setUp()  {
        EditProfileActivity.profileToEdit = new Profile("12342", "", "", "", null, 1, 1, "");
    }

    @Test
    public void closeBtnClickable() {
        onView(withId(R.id.cancelButton)).check(matches(isDisplayed()));
    }
}
