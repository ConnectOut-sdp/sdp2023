package com.sdpteam.connectout.mapList.map;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.event.EventCreatorActivity;
import com.sdpteam.connectout.event.EventFirebaseDataSource;
import com.sdpteam.connectout.event.EventsViewModel;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class MapFragmentTest {

    @Rule
    public ActivityScenarioRule<EventCreatorActivity> testRule = new ActivityScenarioRule<>(EventCreatorActivity.class);

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void cleanup() {
        Intents.release();
    }

    @Test
    public void testMapDisplayed() {
        onView(withId(R.id.map)).check(matches(isDisplayed()));
        onView(withId(R.id.refresh_button)).perform(click());
        onView(withId(R.id.map)).check(matches(isDisplayed()));
    }

    @Test
    public void defaultPositionBeforeMarkerInstantiation() {
        PositionSelectorFragment positionSelectorFragment = new PositionSelectorFragment(new EventsViewModel(new EventFirebaseDataSource()));
        assertThat(positionSelectorFragment.getMovingMarkerPosition().latitude, is(0.0));
        assertThat(positionSelectorFragment.getMovingMarkerPosition().longitude, is(0.0));

    }


}