package com.sdpteam.connectout.profile;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static com.sdpteam.connectout.profile.ProfileRateActivity.RATED_NAME;
import static com.sdpteam.connectout.profile.ProfileRateActivity.RATED_UID;
import static com.sdpteam.connectout.utils.FutureUtils.fJoin;
import static com.sdpteam.connectout.utils.FutureUtils.waitABit;
import static com.sdpteam.connectout.utils.RandomPath.generateRandomPath;
import static org.junit.Assert.assertEquals;

import android.content.Intent;
import android.widget.RatingBar;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdpteam.connectout.R;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

/**
 * End to end test for the profile rate activity
 */
@RunWith(AndroidJUnit4.class)
public class ProfileRateTest {

    private final static String uid = generateRandomPath();
    private final static String name = "test";
    static Intent intent;

    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), ProfileRateActivity.class);
        intent.putExtra(RATED_UID, uid);
        intent.putExtra(RATED_NAME, name);
    }

    private static final ProfileFirebaseDataSource model = new ProfileFirebaseDataSource();
    @Rule
    public ActivityScenarioRule<ProfileRateActivity> testRule = new ActivityScenarioRule<>(intent);

    @BeforeClass
    public static void setup() {
        Profile testProfile = new Profile(uid, name, "test@gmail.com", "test",
                Profile.Gender.MALE, 0, 0, "");
        fJoin(model.saveProfile(testProfile));
    }

    @AfterClass
    public static void cleanUp() {
        model.deleteProfile(uid);
    }

    @Test
    public void testRatingElements() {
        onView(ViewMatchers.withId(R.id.simpleRatingBar)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.submitRatingButton)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.rateUserTextView)).check(matches(isDisplayed()));
        onView(ViewMatchers.withId(R.id.reportUser)).check(matches(isDisplayed()));
    }

    @Test
    public void testRating() {
        // set rating to 3
        testRule.getScenario().onActivity(activity -> {
            RatingBar ratingBar = activity.findViewById(R.id.simpleRatingBar);
            ratingBar.setRating(3);
        });
        onView(ViewMatchers.withId(R.id.submitRatingButton)).perform(click());
        waitABit();
        assertEquals(fJoin(model.fetchProfile(uid)).getRating(), 3, 0.001);
        assertEquals(fJoin(model.fetchProfile(uid)).getNumRatings(), 1);
    }
}
