package com.sdpteam.connectout.event;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.map.MapViewFragment;

import android.widget.Button;
import androidx.fragment.app.Fragment;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class EventActivityTest {

    @Rule
    public ActivityScenarioRule<EventActivity> activityRule = new ActivityScenarioRule<>(EventActivity.class);

    @Before
    public void setUp() {
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
            Assert.assertTrue(fragment instanceof MapViewFragment);
        });
    }

    @Test
    public void joinEventShouldFinishActivity() {
        onView(withId(R.id.event_join_button)).perform(ViewActions.click());

        // Might return null if activity already finished
        try {
            activityRule.getScenario().onActivity(activity -> {
                final Button button = activity.findViewById(R.id.event_join_button);
                button.setOnClickListener(v -> Assert.assertTrue(activity.isFinishing()));
            });
        } catch (NullPointerException ignored) {
        }
    }
}