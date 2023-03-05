package com.sdpteam.connectout.map;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import org.junit.Test;
import org.junit.runner.RunWith;

import com.sdpteam.connectout.R;

import android.os.Handler;
import android.os.Looper;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

@RunWith(AndroidJUnit4.class)
@LargeTest //without that does not seem to work :/
public class MapFragmentTest {

    @Test
    public void testViewDisplayed() {
        FragmentScenario<MapFragment> scenario = FragmentScenario.launchInContainer(MapFragment.class);
        scenario.onFragment(fragment -> {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> {
                onView(withId(R.id.map)).check(matches(isDisplayed()));
                onView(withId(R.id.refresh_button)).perform(click());
                onView(withId(R.id.map)).check(matches(isDisplayed()));
            });
        });
    }
}
