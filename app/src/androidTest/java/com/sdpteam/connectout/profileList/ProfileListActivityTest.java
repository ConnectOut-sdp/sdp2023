package com.sdpteam.connectout.profileList;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.sdpteam.connectout.utils.WithIndexMatcher.withIndex;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.greaterThan;
import static org.hamcrest.Matchers.is;
import static org.hamcrest.Matchers.not;

import android.os.Looper;
import android.widget.ListView;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileActivity;
import com.sdpteam.connectout.profile.ProfileFirebaseDataSource;
import com.sdpteam.connectout.utils.LiveDataTestUtil;
import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileActivity;
import com.sdpteam.connectout.profile.ProfileFirebaseDataSource;
import com.sdpteam.connectout.utils.LiveDataTestUtil;

import android.os.Handler;
import android.os.Looper;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.espresso.intent.Intents;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
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

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void cleanup() {
        Intents.release();
    }


    @Rule
    public ActivityScenarioRule<ProfileListActivity> activityRule = new ActivityScenarioRule<>(ProfileListActivity.class);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();



    @Test
    public void clickingOnAUserLaunchesRightProfilePage() {
        onView(ViewMatchers.withId(R.id.container_users_listview)).check(matches(isDisplayed()));
        onView(withId(R.id.container_users_listview)).check(matches(isDisplayed()));

        ProfileListViewModel model = new ProfileListViewModel(new ProfileFirebaseDataSource());
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> model.getListOfProfile(ProfileFirebaseDataSource.ProfileOrderingOption.NONE, null)); // set up the live data in main thread (because we cannot invoke [LiveData].setValue on a background thread)

        List<Profile> list = LiveDataTestUtil.getOrAwaitValue(model.getUserListLiveData());
        assertThat(list.size(), greaterThan(0)); // empty list in firebase not excepted for testing
        int userIndexToCheck = 0;

        String expectedUid = list.get(userIndexToCheck).getId();

        onView(withIndex(withId(R.id.nameAdapterTextView), userIndexToCheck)).perform(click());
        intended(Matchers.allOf(hasComponent(ProfileActivity.class.getName()), hasExtra(equalTo("uid"), equalTo(expectedUid))));
    }


    @Test
    public void testListUsersDisplayed() {
        onView(ViewMatchers.withId(R.id.container_users_listview)).check(matches(isDisplayed()));
        onView(withId(R.id.container_users_listview)).check(matches(isDisplayed()));
    }

    @Rule
    public ActivityScenarioRule<ProfileListActivity> activityScenarioRule = new ActivityScenarioRule<>(ProfileListActivity.class);

    @Test
    public void initialViewIsFiltered() {
        onView(withId(R.id.user_list_button)).check(matches((isChecked())));
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
        onView(withId(R.id.user_list_button)).perform(click());
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
            assertThat(givenList, is(copiedList));
        }
    }

}
