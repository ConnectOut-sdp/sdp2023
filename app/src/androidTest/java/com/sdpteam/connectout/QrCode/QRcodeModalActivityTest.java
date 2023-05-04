package com.sdpteam.connectout.QrCode;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;

import androidx.test.core.app.ActivityScenario;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdpteam.connectout.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class QRcodeModalActivityTest {

    @Rule
    public ActivityScenarioRule<QRcodeModalActivity> activityScenarioRule =
            new ActivityScenarioRule<>(QRcodeModalActivity.class);

    @Test
    public void testComponentsAreCorrectlyDisplayed() {
        String title = "Profile QR code";
        String qrCodeData = "https://connect-out.com/events/0123456789";

        // create the intent with extras
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), QRcodeModalActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("qrCodeData", qrCodeData);

        // launch the activity with the intent
        ActivityScenario<QRcodeModalActivity> activityScenario = ActivityScenario.launch(intent);

        // check if the components are displayed
        onView(withId(R.id.modal_title)).check(matches(isDisplayed()));
        onView(withId(R.id.qrcode_image)).check(matches(isDisplayed()));
        onView(withId(R.id.close_button)).check(matches(isDisplayed()));

        // close the activity
        activityScenario.close();
    }

    @Test
    public void testCloseButton() {
        String title = "Profile QR code";
        String qrCodeData = "https://connect-out.com/events/0123456789";

        // create the intent with extras
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), QRcodeModalActivity.class);
        intent.putExtra("title", title);
        intent.putExtra("qrCodeData", qrCodeData);

        // launch the activity with the intent
        ActivityScenario<QRcodeModalActivity> activityScenario = ActivityScenario.launch(intent);

        onView(withId(R.id.close_button)).perform(click());

        // close the activity
        activityScenario.close();
    }
}
