package com.sdpteam.connectout.map;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;

import android.os.Handler;
import android.os.Looper;

import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.filters.LargeTest;

import com.sdpteam.connectout.R;

import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
@LargeTest
public class MapFragmentTest {

    @Test
    public void testViewDisplayed() {
        FragmentScenario<MapViewFragment> scenario = FragmentScenario.launch(MapViewFragment.class);
        scenario.onFragment(fragment -> {
            Handler handler = new Handler(Looper.getMainLooper());
            handler.post(() -> onView(ViewMatchers.withId(R.id.map)).check(matches(isDisplayed())));
        });
    }
}