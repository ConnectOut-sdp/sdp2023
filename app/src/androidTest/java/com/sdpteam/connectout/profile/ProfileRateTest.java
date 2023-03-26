package com.sdpteam.connectout.profile;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static org.junit.Assert.assertEquals;

import android.content.Intent;
import android.widget.RatingBar;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdpteam.connectout.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * End to end test for the profile rate activity
 */
@RunWith(AndroidJUnit4.class)
public class ProfileRateTest {

    public static String uid = "testuid";
    private final ProfileFirebaseDataSource model = new ProfileFirebaseDataSource();
    private final ProfileViewModel viewModel = new ProfileViewModel(model);

    static Intent intent;

    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), ProfileRateActivity.class);
        intent.putExtra("uid", uid);
    }

    @Rule
    public ActivityScenarioRule<ProfileRateActivity> testRule = new ActivityScenarioRule<>(intent);

    @Before
    public void setup() {
        Profile testProfile = new Profile(uid, "test", "test@gmail.com", "test",
                Profile.Gender.MALE, 0, 0);
        model.saveProfile(testProfile);
    }

    //@After
    public void cleanup() {
        model.deleteProfile(uid);
    }

    @Test
    public void testRatingElements() {
        onView(ViewMatchers.withId(R.id.simpleRatingBar)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.submitRatingButton)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.rateUserTextView)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.reportUser)).check(matches(isDisplayed()));
    }

    //@Test
    public void testRating() {
        // set rating to 3
        testRule.getScenario().onActivity(activity -> {
            RatingBar ratingBar = (RatingBar) activity.findViewById(R.id.simpleRatingBar);
            ratingBar.setRating(3);
        });
        onView(ViewMatchers.withId(R.id.submitRatingButton)).perform(click());
        assertEquals(viewModel.getProfile(uid).getValue().getRating(), 3, 0.001);
        assertEquals(viewModel.getProfile(uid).getValue().getNumRatings(), 1);
    }
}
