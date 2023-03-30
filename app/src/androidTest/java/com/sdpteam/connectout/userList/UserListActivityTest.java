package com.sdpteam.connectout.userList;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.sdpteam.connectout.utils.WithIndexMatcher.withIndex;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.greaterThan;

import java.util.List;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileActivity;
import com.sdpteam.connectout.profile.ProfileFirebaseDataSource;
import com.sdpteam.connectout.utils.LiveDataTestUtil;

import android.os.Handler;
import android.os.Looper;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class UserListActivityTest {

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void cleanup() {
        Intents.release();
    }

    @Rule
    public ActivityScenarioRule<UserListActivity> activityRule = new ActivityScenarioRule<>(UserListActivity.class);

    @Test
    public void testListUsersDisplayed() {
        onView(ViewMatchers.withId(R.id.container_users_listview)).check(matches(isDisplayed()));
        onView(withId(R.id.container_users_listview)).check(matches(isDisplayed()));
    }

    @Test
    public void clickingOnAUserLaunchesRightProfilePage() {
        onView(ViewMatchers.withId(R.id.container_users_listview)).check(matches(isDisplayed()));
        onView(withId(R.id.container_users_listview)).check(matches(isDisplayed()));

        UserListViewModel model = new UserListViewModel(new ProfileFirebaseDataSource());
        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(model::triggerFetchProfileSortedByRating); // set up the live data in main thread (because we cannot invoke [LiveData].setValue on a background thread)

        List<Profile> list = LiveDataTestUtil.getOrAwaitValue(model.getUserListLiveData());
        assertThat(list.size(), greaterThan(0)); // empty list in firebase not excepted for testing
        int userIndexToCheck = 0;

        String expectedUid = list.get(userIndexToCheck).getId();

        onView(withIndex(withId(R.id.nameAdapterTextView), userIndexToCheck)).perform(click());
        intended(Matchers.allOf(hasComponent(ProfileActivity.class.getName()), hasExtra(equalTo("uid"), equalTo(expectedUid))));
    }
}
