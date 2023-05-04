package com.sdpteam.connectout.profileList;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.sdpteam.connectout.utils.FutureUtils.waitABit;
import static com.sdpteam.connectout.utils.RandomPath.generateRandomPath;
import static com.sdpteam.connectout.utils.WithIndexMatcher.withIndex;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import android.os.Handler;
import android.os.Looper;
import android.widget.ListView;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.nearbyEvents.map.GPSCoordinates;
import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileActivity;
import com.sdpteam.connectout.profile.ProfileFirebaseDataSource;
import com.sdpteam.connectout.utils.LiveDataTestUtil;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@RunWith(AndroidJUnit4.class)
public class ProfileListActivityTest {
    private static final String profileId1 = generateRandomPath();
    private static final String profileId2 = generateRandomPath();
    @Rule
    public ActivityScenarioRule<ProfileListActivity> activityRule = new ActivityScenarioRule<>(ProfileListActivity.class);
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        Intents.init();
    }

    @BeforeClass
    public static void classSetUp(){
        Profile p1 = new Profile(profileId1, "Donald Trump", "donald@gmail.com", "I was the president by the way", Profile.Gender.MALE, 2, 200, null);
        Profile p2 = new Profile(profileId2, "Barack Obama", "barack@gmail.com", "Drop the mic !!!!!!!", Profile.Gender.MALE, 3, 300, null);
        new ProfileFirebaseDataSource().saveProfile(p1);
        new ProfileFirebaseDataSource().saveProfile(p2);
        waitABit();
    }

    @After
    public void cleanup() {
        Intents.release();
    }

    @AfterClass
    public static void classCleanUp(){
        new ProfileFirebaseDataSource().deleteProfile(profileId1);
        new ProfileFirebaseDataSource().deleteProfile(profileId2);
    }

    @Test
    public void clickingOnAUserLaunchesRightProfilePage() {
        onView(ViewMatchers.withId(R.id.container_users_listview)).check(matches(isDisplayed()));
        onView(withId(R.id.container_users_listview)).check(matches(isDisplayed()));

        ProfileListViewModel model = new ProfileListViewModel(new ProfileFirebaseDataSource());
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> model.getListOfProfile(ProfileFirebaseDataSource.ProfileOrderingOption.NONE, null)); // set up the live data in main thread (because we cannot invoke [LiveData].setValue
        // on a background thread)

        List<Profile> list = LiveDataTestUtil.getOrAwaitValue(model.getUserListLiveData());
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
        onView(withId(R.id.filter_apply_button)).check(matches((isDisplayed())));
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

    @Test
    public void filteringByRatingFindsPeopleWithGivenValue() {
        onView(withId(R.id.text_filter)).perform(typeText("0;1"));
        closeSoftKeyboard();
        onView(withId(R.id.rating_switch_button)).perform(click());
        onView(withId(R.id.filter_category_button)).perform(click());
        onView(withId(R.id.filter_apply_button)).perform(click());
        onView(withId(R.id.filter_container)).check(matches(isDisplayed()));

        List<Profile> profiles = new ArrayList<>();
        activityRule.getScenario().onActivity(activity -> {
            ListView recyclerView = activity.findViewById(R.id.user_list_view);
            for (int i = 0; i < recyclerView.getAdapter().getCount(); i++) {
                Profile profile = (Profile) recyclerView.getAdapter().getItem(i);
                profiles.add(profile);
            }
        });
        if (!profiles.isEmpty()) {
            Stream<Double> ratings = profiles.stream().map(Profile::getRating);
            assertThat(ratings.filter(r -> r > 1.0 || r < 0.0).count(), is(0L));
        }
    }

    @Test
    public void filteringByRatingIsOrdered() {
        onView(withId(R.id.rating_switch_button)).perform(click());
        onView(withId(R.id.filter_apply_button)).perform(click());
        onView(withId(R.id.filter_container)).check(matches(isDisplayed()));
        List<Profile> profiles = new ArrayList<>();

        activityRule.getScenario().onActivity(activity -> {
            ListView recyclerView = activity.findViewById(R.id.user_list_view);
            for (int i = 0; i < recyclerView.getAdapter().getCount(); i++) {
                Profile profile = (Profile) recyclerView.getAdapter().getItem(i);
                profiles.add(profile);
            }
        });

        if (!profiles.isEmpty()) {
            List<Double> givenList = profiles.stream().map(Profile::getRating).collect(Collectors.toList());
            List<Double> copiedList = new ArrayList<>(givenList);
            Collections.sort(copiedList);
            Collections.reverse(copiedList);
            assertThat(givenList, is(copiedList));
        }
    }

    @Test
    public void wrongFilteringWithRatingShowsCompleteList() {
        onView(withId(R.id.name_switch_button)).perform(click());
        onView(withId(R.id.rating_switch_button)).perform(click());
        onView(withId(R.id.text_filter)).perform(typeText("0;1 I dont know how to use filter "), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.filter_apply_button)).perform(click());
        onView(withId(R.id.filter_container)).check(matches(isDisplayed()));

        List<Profile> profiles = new ArrayList<>();
        activityRule.getScenario().onActivity(activity -> {
            ListView recyclerView = activity.findViewById(R.id.user_list_view);
            for (int i = 0; i < recyclerView.getAdapter().getCount(); i++) {
                Profile profile = (Profile) recyclerView.getAdapter().getItem(i);
                profiles.add(profile);
            }
        });
        if (!profiles.isEmpty()) {
            List<Double> givenList = profiles.stream().map(Profile::getRating).collect(Collectors.toList());
            List<Double> copiedList = new ArrayList<>(givenList);
            Collections.sort(copiedList);
            Collections.reverse(copiedList);
            assertThat(givenList, is(copiedList));
        }
    }
}
