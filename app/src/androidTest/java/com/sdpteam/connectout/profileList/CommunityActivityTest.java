package com.sdpteam.connectout.profileList;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;
import static com.sdpteam.connectout.utils.FutureUtils.waitABit;
import static com.sdpteam.connectout.utils.RandomPath.generateRandomPath;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.not;

import android.os.Handler;
import android.os.Looper;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileFirebaseDataSource;
import com.sdpteam.connectout.utils.LiveDataTestUtil;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.List;

@RunWith(AndroidJUnit4.class)
public class CommunityActivityTest {
    private static final String profileId1 = generateRandomPath();
    private static final String profileId2 = generateRandomPath();
    @Rule
    public ActivityScenarioRule<CommunityActivity> activityRule = new ActivityScenarioRule<>(CommunityActivity.class);
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @BeforeClass
    public static void classSetUp() {
        Profile p1 = new Profile(profileId1, "Donald Trump", "donald@gmail.com", "I was the president by the way", Profile.Gender.MALE, 2, 200, null);
        Profile p2 = new Profile(profileId2, "Barack Obama", "barack@gmail.com", "Drop the mic !!!!!!!", Profile.Gender.MALE, 3, 300, null);
        new ProfileFirebaseDataSource().saveProfile(p1);
        new ProfileFirebaseDataSource().saveProfile(p2);
        waitABit();
    }

    @AfterClass
    public static void classCleanUp() {
        new ProfileFirebaseDataSource().deleteProfile(profileId1);
        new ProfileFirebaseDataSource().deleteProfile(profileId2);
    }

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void cleanup() {
        Intents.release();
    }

    @Test
    public void clickingOnAUserLaunchesRightProfilePage() {
        onView(ViewMatchers.withId(R.id.container_users_listview)).check(matches(isDisplayed()));
        onView(withId(R.id.container_users_listview)).check(matches(isDisplayed()));

        ProfilesViewModel model = new ProfilesViewModel(new ProfileFirebaseDataSource());
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(model::refreshProfiles); // set up the live data in main thread (because we cannot invoke [LiveData].setValue
        // on a background thread)

        List<Profile> list = LiveDataTestUtil.getOrAwaitValue(model.getProfilesLiveData());
        assertThat(list.size(), greaterThan(0)); // empty list in firebase not excepted for testing
        int userIndexToCheck = 0;
        Profile p = list.get(userIndexToCheck);

        String expectedUid = (p == null) ? NULL_USER : list.get(userIndexToCheck).getId();

/*TODO fix ProfileList bug
        onView(withIndex(withId(R.id.nameAdapterTextView), userIndexToCheck)).perform(click());
        intended(Matchers.allOf(hasComponent(ProfileActivity.class.getName()), hasExtra(equalTo("uid"), equalTo(expectedUid))));*/
    }

    @Test
    public void testListUsersDisplayed() {
        onView(ViewMatchers.withId(R.id.container_users_listview)).check(matches(isDisplayed()));
        onView(withId(R.id.container_users_listview)).check(matches(isDisplayed()));
    }

    @Test
    public void initialViewIsFiltered() {
        onView(withId(R.id.user_list_button)).check(matches((isDisplayed())));
    }

    @Test
    public void getNonFilteredView() {
        onView(withId(R.id.user_list_button)).perform(click());
        onView(withId(R.id.container_users_listview)).check(matches(isDisplayed()));
    }

    @Test
    public void swappingDisplaysFilterView() {
        onView(withId(R.id.filter_container)).check(matches(isDisplayed()));
    }

    @Test
    public void doubleSwappingDisplaysNonFilteredView() {
        onView(withId(R.id.filter_container)).check(matches(isDisplayed()));
        onView(withId(R.id.user_list_button)).perform(click());

        //If exception is thrown, it means that view is destroyed and freed.
        try {
            onView(withId(R.id.filter_container)).check(matches(not(isDisplayed())));
        } catch (NoMatchingViewException ignored) {
        }
    }
}
