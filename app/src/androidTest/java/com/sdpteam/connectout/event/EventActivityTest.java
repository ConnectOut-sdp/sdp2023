package com.sdpteam.connectout.event;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.sdpteam.connectout.event.viewer.EventActivity.JOIN_EVENT;
import static com.sdpteam.connectout.event.viewer.EventActivity.LEAVE_EVENT;
import static com.sdpteam.connectout.event.viewer.EventActivity.PASSED_ID_KEY;
import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Intent;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.event.nearbyEvents.map.GPSCoordinates;
import com.sdpteam.connectout.event.viewer.EventActivity;
import com.sdpteam.connectout.event.viewer.EventMapViewFragment;
import com.sdpteam.connectout.profileList.ProfileFilterFragment;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.atomic.AtomicReference;

@RunWith(AndroidJUnit4.class)
public class EventActivityTest {

    private final static Event TEST_EVENT = new Event("154", "event1", "descr", new GPSCoordinates(1.2, 1.2), "Bob");


    @Rule
    public ActivityScenarioRule<EventActivity> activityRule = new ActivityScenarioRule<>(new Intent(ApplicationProvider.getApplicationContext(), EventActivity.class).putExtra(PASSED_ID_KEY, TEST_EVENT.getId()));
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() {
        new EventFirebaseDataSource().saveEvent(TEST_EVENT);
        Intents.init();

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


    // @Test
    public void consecutiveJoinAndLeaveEventChangesBelongingUser() {
        //Used to retrieve the button's text which indicates what operation occurred
        final AtomicReference<String> buttonText = new AtomicReference<>(null);

        findButtonText(buttonText);

        //Loop twice to do both operations consecutively.
        for (int i = 0; i < 2; i++) {

            // Perform the click on the button
            onView(withId(R.id.event_join_button)).perform(ViewActions.click());
            onView(withId(R.id.refresh_button)).perform(ViewActions.click());

            //Find the new text
            findButtonText(buttonText);


            Event obtained = new EventFirebaseDataSource().getEvent(TEST_EVENT.getId()).join();

            //Checks given the button text weather the user should be in the list or not
            if (buttonText.get().equals(JOIN_EVENT)) {
                assertFalse(obtained.getParticipants().contains(NULL_USER));
            } else {
                assertTrue(obtained.getParticipants().contains(NULL_USER));
            }

            onView(withId(R.id.refresh_button)).perform(ViewActions.click());

        }


    }

    @Test
    public void clickToolbarIconFinishesActivity() {
        activityRule.getScenario().onActivity(activity -> {
            Toolbar toolbar = activity.findViewById(R.id.event_toolbar);
            toolbar.setNavigationOnClickListener(v -> Assert.assertTrue(activity.isFinishing()));
            toolbar.performClick();
        });
    }

    private void findButtonText(AtomicReference<String> buttonText){
        activityRule.getScenario().onActivity(activity -> {
            Button b = activity.findViewById(R.id.event_join_button);
            buttonText.set(waitNewButtonTextUpdate(b, buttonText.get()));
        });
    }
    private String waitNewButtonTextUpdate(Button b, String OldText) {
        String text = null;
        if (OldText == null) {
            while (!JOIN_EVENT.equals(text) && !LEAVE_EVENT.equals(text)) {
                text = b.getText().toString();
            }
            return text;
        }else{
            String needed = JOIN_EVENT.equals(OldText) ? LEAVE_EVENT : JOIN_EVENT;;
            while (!needed.equals(text) ) {
                text = b.getText().toString();
            }
            return text;
        }
    }

}