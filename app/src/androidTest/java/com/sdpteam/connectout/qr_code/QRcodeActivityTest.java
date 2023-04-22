package com.sdpteam.connectout.qr_code;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.junit.Assert.assertNotNull;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sdpteam.connectout.R;

import android.widget.Button;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class QRcodeActivityTest {

    @Rule
    public ActivityScenarioRule<QRcodeActivity> activityScenarioRule = new ActivityScenarioRule<>(QRcodeActivity.class);

    @Test
    public void isButtonNotNull() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            Button button = activity.findViewById(R.id.close_button);
            if (button != null) {
                assertNotNull(button);
            }
        });
    }

    @Test
    public void isButtonDisplayed() {
        onView(withId(R.id.btn_scan)).check(matches(isDisplayed()));
    }

    @Test
    public void isButtonClickable() {
        onView(withId(R.id.btn_scan)).check(matches(isClickable()));
    }
}




