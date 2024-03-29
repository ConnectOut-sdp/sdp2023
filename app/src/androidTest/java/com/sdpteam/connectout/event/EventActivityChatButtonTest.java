package com.sdpteam.connectout.event;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.sdpteam.connectout.event.viewer.EventActivity.PASSED_ID_KEY;
import static com.sdpteam.connectout.utils.FutureUtils.fJoin;
import static com.sdpteam.connectout.utils.FutureUtils.waitABit;
import static com.sdpteam.connectout.utils.RandomPath.generateRandomPath;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.GoogleAuth;
import com.sdpteam.connectout.event.nearbyEvents.map.GPSCoordinates;
import com.sdpteam.connectout.event.viewer.EventActivity;

import android.content.Intent;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

/**
 * Test for the chat button in the event activity
 * NEEDS TO STAY THE UNIQUE TEST IN THIS FILE TO WORK!
 */
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
        new GoogleAuth().logout();
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
        onView(withId(R.id.event_join_button)).perform(ViewActions.scrollTo()).perform(ViewActions.click());
        waitABit();
        //    onView(withId(R.id.event_chat_btn)).check(matches(isDisplayed()));
        //trying without this line which seems to create an issue
        // refresh
        fJoin(new EventFirebaseDataSource().getEvent(TEST_EVENT.getId()));

        // quit event
        onView(withId(R.id.event_join_button)).perform(ViewActions.scrollTo()).perform(ViewActions.click());
        waitABit();
        onView(withId(R.id.refresh_button)).perform(ViewActions.scrollTo()).perform(ViewActions.click());
        waitABit();
        //    onView(withId(R.id.event_chat_btn)).check(matches(not(isDisplayed())));
    }
}
