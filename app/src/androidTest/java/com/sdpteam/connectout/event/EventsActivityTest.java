package com.sdpteam.connectout.event;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sdpteam.connectout.R;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class EventsActivityTest {

    @Rule
    public ActivityScenarioRule<EventsActivity> testRule = new ActivityScenarioRule<>(EventsActivity.class);

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void cleanup() {
        Intents.release();
    }

    @Test
    public void mapIsDisplayedInitially() {
        onView(ViewMatchers.withId(R.id.map)).check(matches(isDisplayed()));
    }

    @Test
    public void switchButtonChangesToListView() {
        onView(withId(R.id.list_switch_button)).perform(click());
        onView(withId(R.id.event_list)).check(matches(isDisplayed()));
    }

    @Test
    public void switchButtonDoubleClickReturnToMapView() {
        // Click the switch button twice to return to the map view
        onView(withId(R.id.list_switch_button)).perform(click());
        onView(withId(R.id.event_list)).check(matches(isDisplayed()));
        onView(withId(R.id.map_switch_button)).perform(click());
        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }
}
