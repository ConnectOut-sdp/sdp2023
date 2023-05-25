package com.sdpteam.connectout.event;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.not;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.event.nearbyEvents.EventsActivity;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.espresso.NoMatchingViewException;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

@RunWith(AndroidJUnit4.class)
public class EventsFilterDialogTest {

    @Rule
    public ActivityScenarioRule<EventsActivity> testRule = new ActivityScenarioRule<>(EventsActivity.class);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Rule
    public GrantPermissionRule grantLocationRule = GrantPermissionRule.grant(ACCESS_FINE_LOCATION, ACCESS_NETWORK_STATE, ACCESS_COARSE_LOCATION);

    @Test
    public void popupIsShownOnFilterBtn() {
        // open filter popup
        onView(withId(R.id.events_filter_button)).perform(click());

        // check if filter is correctly rendered
        onView(withId(R.id.popup_events_filter)).check(matches(isDisplayed()));
        onView(withId(R.id.events_filter_search)).check(matches(isDisplayed()));
        onView(withId(R.id.events_filter_seekbar)).check(matches(isDisplayed()));
        onView(withId(R.id.events_filter_apply_btn)).check(matches(isDisplayed()));

        // apply filter and close
        onView(withId(R.id.events_filter_apply_btn)).perform(click());
        try {
            onView(withId(R.id.popup_events_filter)).check(matches(not(isDisplayed())));
        } catch (NoMatchingViewException ignored) {
        }
    }

    @Test
    public void changeFiltersIsPersistent() {
        // open filter popup
        onView(withId(R.id.events_filter_button)).perform(click());

        // set text filter
        onView(withId(R.id.events_filter_search)).check(matches(isDisplayed()));
        onView(withId(R.id.events_filter_search)).perform(typeText("tennis"));

        // apply filter and close
        onView(withId(R.id.events_filter_apply_btn)).perform(click());

        // reopen filter popup
        onView(withId(R.id.events_filter_button)).perform(click());

        // check persistence
        onView(withId(R.id.events_filter_search)).check(matches(withText("tennis")));
    }
}
