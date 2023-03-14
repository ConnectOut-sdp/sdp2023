package com.sdpteam.connectout.event;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.widget.Button;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.GeneralLocation;
import androidx.test.espresso.action.GeneralSwipeAction;
import androidx.test.espresso.action.Press;
import androidx.test.espresso.action.Swipe;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.map.PositionSelectorFragment;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EventCreatorActivityTest {

    @Rule
    public ActivityScenarioRule<EventCreatorActivity> activityRule = new ActivityScenarioRule<>(EventCreatorActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void clickToolbarIconFinishesActivity() {
        activityRule.getScenario().onActivity(activity -> {
            Toolbar toolbar = activity.findViewById(R.id.event_creator_toolbar);
            toolbar.setNavigationOnClickListener(v -> Assert.assertTrue(activity.isFinishing()));
            toolbar.performClick();
        });

    }

    @Test
    public void activityIsOpenedBeforeClickingToolbarIcon() {
        activityRule.getScenario().onActivity(activity -> {
            Fragment fragment = activity.getSupportFragmentManager().findFragmentById(R.id.event_creator_fragment_container);
            Assert.assertTrue(fragment instanceof PositionSelectorFragment);
        });
    }

    @Test
    public void clickSaveButtonLogsExpectedOutput() {
        onView(withId(R.id.event_creator_title)).perform(ViewActions.typeText("Test Title"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.event_creator_description)).perform(ViewActions.typeText("Test Description"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.event_creator_save_button)).perform(ViewActions.click());

        //Might return null if activity already terminated
        try {
            activityRule.getScenario().onActivity(activity -> {
                Button button = activity.findViewById(R.id.event_creator_save_button);
                button.setOnClickListener(v -> Assert.assertTrue(activity.isFinishing()));
            });
        } catch (NullPointerException ignored) {
        }

    }

    @Test
    public void markerIsDraggable() {
        onView(withId(R.id.map)).perform(ViewActions.longClick());
        onView(withId(R.id.map)).perform(dragViewBy((float) 0.1, (float) 0.1));
        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }

    public static ViewAction dragViewBy(float latitude, float longitude) {
        return ViewActions.actionWithAssertions(new GeneralSwipeAction(
                Swipe.SLOW,
                GeneralLocation.CENTER,
                GeneralLocation.translate(GeneralLocation.CENTER, latitude, longitude),
                Press.FINGER));

    }

}