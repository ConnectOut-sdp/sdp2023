package com.sdpteam.connectout.map;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdpteam.connectout.MainActivity;
import com.sdpteam.connectout.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.TimeUnit;

@RunWith(AndroidJUnit4.class)
public class MapFragmentTest {

    private MainActivity activity;

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }


    @Test
    public void testMapDisplayed() {
        FragmentScenario<MapFragment> scenario = FragmentScenario.launchInContainer(MapFragment.class);
        scenario.onFragment(fragment -> {
            {
                CompletableFuture.runAsync(() -> {
                    onView(ViewMatchers.withId(R.id.map)).check(matches(isDisplayed()));
                    onView(withId(R.id.refresh_button)).perform(click());
                    onView(withId(R.id.map)).check(matches(isDisplayed()));
                }).orTimeout(1, TimeUnit.MINUTES).join();
            }
        });
    }
}
