package com.sdpteam.connectout.userList;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdpteam.connectout.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class UserListActivityTest {

    @Rule
    public ActivityScenarioRule<UserListActivity> activityRule = new ActivityScenarioRule<>(UserListActivity.class);

    @Test
    public void testListUsersDisplayed() {
        onView(ViewMatchers.withId(R.id.container_users_listview)).check(matches(isDisplayed()));
        onView(withId(R.id.container_users_listview)).check(matches(isDisplayed()));
    }
}
