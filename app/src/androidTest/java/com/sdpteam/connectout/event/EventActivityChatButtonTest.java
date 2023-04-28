package com.sdpteam.connectout.event;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.sdpteam.connectout.event.viewer.EventActivity.JOIN_EVENT;
import static com.sdpteam.connectout.event.viewer.EventActivity.LEAVE_EVENT;
import static com.sdpteam.connectout.event.viewer.EventActivity.PASSED_ID_KEY;
import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;
import static com.sdpteam.connectout.utils.FutureUtils.fJoin;
import static com.sdpteam.connectout.utils.FutureUtils.waitABit;
import static com.sdpteam.connectout.utils.RandomPath.generateRandomPath;
import static org.hamcrest.Matchers.not;
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

public class EventActivityChatButtonTest {
    private static String eventTitle1 = generateRandomPath();
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

    @AfterClass
    public static void tearDownClass() {
        new EventFirebaseDataSource().deleteEvent(TEST_EVENT.getId());
    }

    @Test
    public void chatButtonShouldOnlyBeVisibleIfUserJoinedEvent() {
        // join event
        onView(withId(R.id.event_join_button)).perform(ViewActions.click());
        waitABit();
        onView(withId(R.id.event_chat_btn)).check(matches(isDisplayed()));

        // refresh
        fJoin(new EventFirebaseDataSource().getEvent(TEST_EVENT.getId()));

        // quit event
        onView(withId(R.id.event_join_button)).perform(ViewActions.click());
        waitABit();
        onView(withId(R.id.refresh_button)).perform(ViewActions.click());
        waitABit();
        onView(withId(R.id.event_chat_btn)).check(matches(not(isDisplayed())));
    }
}
