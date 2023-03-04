package com.sdpteam.connectout;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.test.espresso.ViewInteraction;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class MapActivityTest {

    @Rule
    public ActivityScenarioRule<MapActivity> mActivityRule = new ActivityScenarioRule<>(MapActivity.class);

    @Test
    public void testMapDisplayed() {
        ViewInteraction mapFragment = onView(withId(R.id.map)).check(matches(isDisplayed()));
        onView(withId(R.id.refresh_button)).perform(click());
        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }
}

