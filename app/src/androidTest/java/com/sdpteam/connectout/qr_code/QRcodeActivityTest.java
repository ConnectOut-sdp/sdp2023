package com.sdpteam.connectout.qr_code;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.doesNotExist;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.RootMatchers.isDialog;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import static org.junit.Assert.assertNotNull;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.sdpteam.connectout.R;

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class QRcodeActivityTest {

    @Rule
    public ActivityScenarioRule<QRcodeActivity> activityScenarioRule = new ActivityScenarioRule<>(QRcodeActivity.class);

    @Test
    public void isButtonNotNull() {
        activityScenarioRule.getScenario().onActivity(activity -> {
            Button button = activity.findViewById(R.id.close_button);
            assertNotNull(button);
        });
    }

    @Test
    public void isButtonDisplayed() {
        onView(withId(R.id.close_button)).check(matches(isDisplayed()));
    }

//    @Test
//    public void testScanCodeButton_launchesScanner() {
//
//        activityScenarioRule.getScenario().onActivity(activity -> {
//            // Find the scan button view
//            final Button scanButton = activity.findViewById(R.id.btn_scan);
//
//            // Perform a click on the scan button
//            activity.runOnUiThread(() -> scanButton.performClick());
//
//            // Check that the scanner is launched
//            final ShadowActivity shadowActivity = Shadows.shadowOf(activityTestRule.getActivity());
//            final Intent startedIntent = shadowActivity.getNextStartedActivity();
//            assertThat(startedIntent.getComponent().getClassName()).isEqualTo(ScanActivity.class.getName());
//        })
//    }


//    private static final ScanOptions expectedOptions = new ScanOptions();
//
//    @Test
//    public void testScanCode() {
//        activityScenarioRule.getScenario().onActivity(activity -> {
//            // Click the scan button
//            onView(withId(R.id.btn_scan)).perform(click());
//
//            // Check that the ScanContract is launched
//            Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, null);
//            Intents.intending(hasComponent(CaptureAct.class.getName())).respondWith(result);
//            intended(hasComponent(CaptureAct.class.getName()));
//
////            // Check that the ScanOptions are correctly set
////            ScanOptions expectedOptions = new ScanOptions();
////            expectedOptions.setPrompt("Volum up to flash on");
////            expectedOptions.setBeepEnabled(true);
////            expectedOptions.setOrientationLocked(true);
////            expectedOptions.setCaptureActivity(CaptureAct.class);
////            intended(hasExtra(ScanContract.EXTRA_OPTIONS, expectedOptions));
////
////            // Simulate a scan result
////            String scanResult = "test result";
////            Intent scanIntent = new Intent();
////            scanIntent.putExtra(Intents.Scan.RESULT, scanResult);
////            Instrumentation.ActivityResult scanResultIntent = new Instrumentation.ActivityResult(Activity.RESULT_OK, scanIntent);
////            Intents.intending(hasComponent(CaptureAct.class.getName())).respondWith(scanResultIntent);
////
////            // Check that the handleScanResult method is called with the correct result
////            onView(withText(scanResult)).inRoot(isDialog()).check(matches(isDisplayed()));
////            onView(withText("OK")).inRoot(isDialog()).perform(click());
//        });
//    }

//    @Test
//    public void testHandleScanResultNull() {
//        activityScenarioRule.getScenario().onActivity(activity -> {
//            activity.handleScanResult(null);
//            // Check that no dialog is shown
//            onView(withText("Result")).check(doesNotExist());
//        });
//    }

//    @Test
//    public void testHandleScanResultNonNull() {
//        activityScenarioRule.getScenario().onActivity(activity -> {
//            String scanResult = "test result";
//            activity.handleScanResult(scanResult);
//            // Check that the dialog is shown with the correct message
//            onView(withText(scanResult)).inRoot(isDialog()).check(matches(isDisplayed()));
//            onView(withText("OK")).inRoot(isDialog()).perform(click());
//        });
//    }
}




