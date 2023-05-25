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
import static com.sdpteam.connectout.event.viewer.EventActivity.PASSED_ID_KEY;
import static com.sdpteam.connectout.utils.FutureUtils.fJoin;
import static com.sdpteam.connectout.utils.FutureUtils.waitABit;
import static com.sdpteam.connectout.utils.RandomPath.generateRandomPath;
import static com.sdpteam.connectout.utils.WithIndexMatcher.withIndex;
import static org.hamcrest.Matchers.equalTo;

import java.util.ArrayList;
import java.util.List;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.event.nearbyEvents.EventsActivity;
import com.sdpteam.connectout.event.nearbyEvents.EventsViewModel;
import com.sdpteam.connectout.event.nearbyEvents.filter.EventFilter;
import com.sdpteam.connectout.event.viewer.EventActivity;
import com.sdpteam.connectout.profile.ProfileActivity;

import android.os.Handler;
import android.os.Looper;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class EventsActivityTest {
    private final String eventId = generateRandomPath();
    private final EventDataSource eventDs = new EventFirebaseDataSource();
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();
    @Rule
    public ActivityScenarioRule<EventsActivity> testRule = new ActivityScenarioRule<>(EventsActivity.class);

    @Before
    public void setup() {
        Intents.init();
        eventDs.saveEvent(new Event(eventId, "Title", "Desc", null, "aa", new ArrayList<>(), new ArrayList<>(), 0, new Event.EventRestrictions(), "https://upload.wikimedia" +
                ".org/wikipedia/commons/thumb/0/08/Flag_of_Switzerland_%28Pantone%29.svg/512px-Flag_of_Switzerland_%28Pantone%29.svg.png"));
        waitABit();
    }

    @After
    public void cleanup() {
        Intents.release();
        eventDs.deleteEvent(eventId);
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
        List<Event> events = fJoin(new EventFirebaseDataSource().getEventsByFilter(EventFilter.NONE));
        int index = 0;
        if (!events.isEmpty()) {
            String expectedTitle = events.get(index).getTitle();
            String expectedDescription = events.get(index).getDescription();
            onView(withIndex(withId(R.id.event_list_event_title), index)).check(matches(withText(expectedTitle)));
            onView(withIndex(withId(R.id.event_list_event_description), index)).check(matches(withText(expectedDescription)));
        }
    }

    @Test
    public void clickingOrganizorProfileInTheListTriggersIntent() {
        onView(withId(R.id.list_switch_button)).perform(click());
        onView(withId(R.id.event_list)).check(matches(isDisplayed()));
        onView(withId(R.id.events_list_view)).check(matches(isDisplayed()));
        List<Event> events = fJoin(new EventFirebaseDataSource().getEventsByFilter(EventFilter.NONE));
        int listIdx = 0;
        if (!events.isEmpty()) {
            String expectedOrganizer = events.get(listIdx).getOrganizer();
            onView(withIndex(withId(R.id.event_list_profile_button), listIdx)).perform(click());
            intended(Matchers.allOf(hasComponent(ProfileActivity.class.getName()), hasExtra(equalTo("uid"), equalTo(expectedOrganizer))));
        }
    }

    @Test
    public void clickMapAddButtonLaunchesCreateActivity() {
        onView(withId(R.id.map_switch_button)).perform(click());
        onView(withId(R.id.events_map_add_button)).perform(click());
        onView(withId(R.id.event_creator_title)).check(matches(isDisplayed()));
    }

    @Test
    public void clickListAddButtonLaunchesCreateActivity() {
        onView(withId(R.id.list_switch_button)).perform(click());
        onView(withId(R.id.events_list_add_button)).perform(click());
        onView(withId(R.id.event_creator_title)).check(matches(isDisplayed()));
    }

    @Test
    public void clickingOnAEventLaunchesRightEventPage() {
        onView(withId(R.id.list_switch_button)).perform(click());
        onView(ViewMatchers.withId(R.id.events_list_view)).check(matches(isDisplayed()));
        onView(withId(R.id.events_list_view)).check(matches(isDisplayed()));

        EventsViewModel model = new EventsViewModel(eventDs);

        Handler handler = new Handler(Looper.getMainLooper());
        handler.post(() -> model.refreshEvents()); // set up the live data in main thread (because we cannot invoke [LiveData].setValue on a background thread)
        waitABit();
        waitABit();
        List<Event> events = model.getEventListLiveData().getValue();
        if (!events.isEmpty()) {
            int userIndexToCheck = 0;
            String expectedUid = events.get(userIndexToCheck).getId();
            onView(withIndex(withId(R.id.event_list_event_title), userIndexToCheck)).perform(click());
            intended(Matchers.allOf(hasComponent(EventActivity.class.getName()), hasExtra(equalTo(PASSED_ID_KEY), equalTo(expectedUid))));
        }
    }

    @Test
    public void clickOnFilterLaunchedCorrectlyWithDate() {
        onView(withId(R.id.list_switch_button)).perform(click());
        onView(ViewMatchers.withId(R.id.events_filter_button)).perform(click());
        waitABit();
        onView(withId(R.id.popup_events_filter)).check(matches(isDisplayed()));

        onView(withId(R.id.filter_in_date)).perform(ViewActions.typeText("10-10-2023"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.events_filter_apply_btn)).perform(click());
    }

    @Test
    public void clickOnFilterLaunchedCorrectlyWithNoFilter() {
        onView(withId(R.id.list_switch_button)).perform(click());
        onView(ViewMatchers.withId(R.id.events_filter_button)).perform(click());
        onView(withId(R.id.popup_events_filter)).check(matches(isDisplayed()));
        onView(withId(R.id.events_filter_apply_btn)).perform(click());
    }

    @Test
    public void clickOnFilterLaunchedCorrectlyWithWrongDate() {
        onView(withId(R.id.list_switch_button)).perform(click());
        onView(ViewMatchers.withId(R.id.events_filter_button)).perform(click());
        onView(withId(R.id.popup_events_filter)).check(matches(isDisplayed()));

        onView(withId(R.id.filter_in_date)).perform(ViewActions.typeText("11"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.events_filter_apply_btn)).perform(click());
    }
}