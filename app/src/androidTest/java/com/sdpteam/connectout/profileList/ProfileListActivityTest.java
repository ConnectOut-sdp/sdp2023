package com.sdpteam.connectout.profileList;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isChecked;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.not;

import androidx.test.espresso.NoMatchingViewException;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdpteam.connectout.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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

    @Test
    public void testListUsersDisplayed() {
        onView(ViewMatchers.withId(R.id.container_users_listview)).check(matches(isDisplayed()));
        onView(withId(R.id.container_users_listview)).check(matches(isDisplayed()));
    }

    @Rule
    public ActivityScenarioRule<ProfileListActivity> activityScenarioRule = new ActivityScenarioRule<>(ProfileListActivity.class);

    @Test
    public void initialViewIsNotFiltered() {
        onView(withId(R.id.user_list_button)).check(matches(not(isChecked())));
    }

    @Test
    public void swappingDisplaysFilterView() {
        onView(withId(R.id.user_list_button)).perform(click());
        onView(withId(R.id.filter_container)).check(matches(isDisplayed()));
    }

    @Test
    public void doubleSwappingDisplaysNonFilteredView() {
        onView(withId(R.id.user_list_button)).perform(click());
        onView(withId(R.id.filter_container)).check(matches(isDisplayed()));
        onView(withId(R.id.user_list_button)).perform(click());

        //If exception is thrown, it means that view is destroyed and freed.
        try {
            onView(withId(R.id.filter_container)).check(matches(not(isDisplayed())));
        } catch (NoMatchingViewException ignored) {
        }
    }

}