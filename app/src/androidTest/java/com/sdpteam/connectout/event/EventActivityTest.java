package com.sdpteam.connectout.event;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.sdpteam.connectout.event.viewer.EventActivity.JOIN_EVENT;
import static com.sdpteam.connectout.event.viewer.EventActivity.LEAVE_EVENT;
import static com.sdpteam.connectout.event.viewer.EventActivity.PASSED_ID_KEY;
import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;
import static com.sdpteam.connectout.utils.FutureUtils.fJoin;
import static com.sdpteam.connectout.utils.FutureUtils.waitABit;
import static com.sdpteam.connectout.utils.RandomPath.generateRandomPath;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Intent;

import androidx.appcompat.widget.Toolbar;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.fragment.app.Fragment;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.event.nearbyEvents.map.GPSCoordinates;
import com.sdpteam.connectout.event.viewer.EventActivity;
import com.sdpteam.connectout.event.viewer.EventMapViewFragment;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EventActivityTest {
    private final static String eventTitle1 = generateRandomPath();
    private final static Event TEST_EVENT = new Event(generateRandomPath(), eventTitle1, "descr", new GPSCoordinates(1.2, 1.2), "Bob");

    @Rule
    public ActivityScenarioRule<EventActivity> activityRule = new ActivityScenarioRule<>(new Intent(ApplicationProvider.getApplicationContext(), EventActivity.class).putExtra(PASSED_ID_KEY,
            TEST_EVENT.getId()));
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @BeforeClass
    public static void setUpClass() {
        new EventFirebaseDataSource().saveEvent(TEST_EVENT);
        waitABit();
    }

    @Before
    public void setUp() {
        new EventFirebaseDataSource().saveEvent(TEST_EVENT);
        waitABit();
        Intents.init();
    }

    @AfterClass
    public static void tearDownClass() {
        new EventFirebaseDataSource().deleteEvent(TEST_EVENT.getId());
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void fragmentIsCorrectlyAdded() {
        activityRule.getScenario().onActivity(activity -> {
            final Fragment fragment = activity.getSupportFragmentManager().findFragmentById(R.id.event_fragment_container);
            assertTrue(fragment instanceof EventMapViewFragment);
        });
        onView(withId(R.id.map)).perform(ViewActions.click());
        onView(withId(R.id.refresh_button)).perform(ViewActions.click());
    }

    @Test
    public void fragmentAddsEventCorrectly() {
        activityRule.getScenario().onActivity(activity -> {
            final Fragment fragment = activity.getSupportFragmentManager().findFragmentById(R.id.event_fragment_container);
            assertTrue(fragment instanceof EventMapViewFragment);
            EventMapViewFragment mapViewFragment = (EventMapViewFragment) fragment;
            mapViewFragment.showEventOnMap(TEST_EVENT);
        });
        onView(withId(R.id.map)).perform(ViewActions.click());
        onView(withId(R.id.refresh_button)).perform(ViewActions.click());
    }

    @Test
    public void fragmentDoesNotCrashWithNullMap() {
        activityRule.getScenario().onActivity(activity -> {
            final Fragment fragment = activity.getSupportFragmentManager().findFragmentById(R.id.event_fragment_container);
            assertTrue(fragment instanceof EventMapViewFragment);
            EventMapViewFragment mapViewFragment = (EventMapViewFragment) fragment;
            mapViewFragment.onMapReady(null);
            mapViewFragment.showEventOnMap(TEST_EVENT);
        });
    }

    @Test
    public void consecutiveJoinAndLeaveEventChangesBelongingUser() {
        //join event
        onView(withId(R.id.event_join_button)).check(matches(withText(JOIN_EVENT)));
        onView(withId(R.id.event_join_button)).perform(ViewActions.click());
        waitABit();
        onView(withId(R.id.refresh_button)).perform(ViewActions.click());
        waitABit();
        onView(withId(R.id.event_join_button)).check(matches(withText(LEAVE_EVENT)));
        Event obtained = fJoin(new EventFirebaseDataSource().getEvent(TEST_EVENT.getId()));
        assertTrue(obtained.getParticipants().contains(NULL_USER));
        // leave event
        onView(withId(R.id.event_join_button)).perform(ViewActions.click());
        waitABit();
        waitABit();
        onView(withId(R.id.refresh_button)).perform(ViewActions.click());
        waitABit();
        onView(withId(R.id.event_join_button)).check(matches(withText(JOIN_EVENT)));
        obtained = fJoin(new EventFirebaseDataSource().getEvent(TEST_EVENT.getId()));
        assertFalse(obtained.getParticipants().contains(NULL_USER));
    }

    @Test
    public void clickToolbarIconFinishesActivity() {
        activityRule.getScenario().onActivity(activity -> {
            Toolbar toolbar = activity.findViewById(R.id.event_toolbar);
            toolbar.setNavigationOnClickListener(v -> Assert.assertTrue(activity.isFinishing()));
            toolbar.performClick();
        });
    }

    @Test
    public void deleteEventTest() {
        EventFirebaseDataSource model = new EventFirebaseDataSource();
        model.saveEvent(TEST_EVENT);
        waitABit();
        model.deleteEvent(TEST_EVENT.getId());
        waitABit();
        assertNull(fJoin(model.getEvent(TEST_EVENT.getId())));
        model.saveEvent(TEST_EVENT);
        waitABit();
        model.deleteEvent(TEST_EVENT.getId());
        waitABit();
        model.deleteEvent(NULL_USER, eventTitle1);
        assertNull(fJoin(model.getEvent(TEST_EVENT.getId())));
    }
}