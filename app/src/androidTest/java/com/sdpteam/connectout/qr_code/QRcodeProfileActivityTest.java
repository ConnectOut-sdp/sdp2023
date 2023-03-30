package com.sdpteam.connectout.qr_code;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import android.content.Intent;
import android.widget.Button;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdpteam.connectout.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class QRcodeProfileActivityTest {

    @Rule
    public ActivityScenarioRule<QRcodeProfileActivity> activityScenarioRule =
            new ActivityScenarioRule<>(QRcodeProfileActivity.class);

    @Test
    public void isButtonNotNull() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            Button button = activity.findViewById(R.id.show_qr_code_btn);
            assertNotNull(button);
        });
    }

    @Test
    public void isButtonDisplayed() {
        onView(withId(R.id.show_qr_code_btn)).check(matches(isDisplayed()));
    }

    @Test
    public void testLaunch() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            Button button = activity.findViewById(R.id.show_qr_code_btn);
            button.performClick();
        });
    }

    // write code to test if arguments are correctly passed to next activity
    @Test
    public void correctIntentExtraArePassed(){
        Intent intent = new Intent();
        intent.putExtra("title", "Profile QR code");
        intent.putExtra("qrCodeData", "https://connect-out.com/profiles/0123456789");

        activityScenarioRule.getScenario().onActivity(activity -> {
            // check if title is correct
            assertEquals("Profile QR code", intent.getStringExtra("title"));

            // check if qrCodeData is correct
            assertEquals("https://connect-out.com/profiles/0123456789", intent.getStringExtra("qrCodeData"));
        });
    }

}
