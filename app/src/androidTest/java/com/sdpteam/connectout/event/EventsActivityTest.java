package com.sdpteam.connectout.event;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.equalTo;

import java.util.List;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.hamcrest.TypeSafeMatcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.event.nearbyEvents.EventsActivity;
import com.sdpteam.connectout.profile.ProfileActivity;

import android.view.View;
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

    @Test
    public void firstEventInTheListHasCorrectTitleAndDescription() {
        onView(withId(R.id.list_switch_button)).perform(click());
        onView(withId(R.id.event_list)).check(matches(isDisplayed()));
        onView(withId(R.id.events_list_view)).check(matches(isDisplayed()));
        List<Event> events = new EventFirebaseDataSource().getEventsByFilter(null, null).join();
        String expectedTitle = events.get(0).getTitle();
        String expectedDescription = events.get(0).getDescription();
        onView(withIndex(withId(R.id.event_list_event_title), 0)).check(matches(withText(expectedTitle)));
        onView(withIndex(withId(R.id.event_list_event_description), 0)).check(matches(withText(expectedDescription)));
    }

    @Test
    public void clickingOrganizorProfileInTheListTriggersIntent() {
        onView(withId(R.id.list_switch_button)).perform(click());
        onView(withId(R.id.event_list)).check(matches(isDisplayed()));
        onView(withId(R.id.events_list_view)).check(matches(isDisplayed()));
        List<Event> events = new EventFirebaseDataSource().getEventsByFilter(null, null).join();
        String expectedOrganizer = events.get(0).getOrganizer();
        onView(withIndex(withId(R.id.event_list_profile_button), 0)).perform(click());
        intended(Matchers.allOf(hasComponent(ProfileActivity.class.getName()), hasExtra(equalTo("uid"), equalTo(expectedOrganizer))));
    }

    static Matcher<View> withIndex(final Matcher<View> matcher, final int index) {
        return new TypeSafeMatcher<View>() {
            @Override
            public void describeTo(org.hamcrest.Description description) {
                description.appendText("with index: ");
                description.appendValue(index);
                matcher.describeTo(description);
            }

            int currentIndex = 0;

            @Override
            public boolean matchesSafely(View view) {
                if (matcher.matches(view)) {
                    if (currentIndex == index) {
                        currentIndex++;
                        return true;
                    }
                    currentIndex++;
                }
                return false;
            }
        };
    }
}