package com.sdpteam.connectout.qr_code;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isClickable;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.journeyapps.barcodescanner.CaptureActivity;
import com.journeyapps.barcodescanner.ScanContract;
import com.journeyapps.barcodescanner.ScanOptions;
import com.sdpteam.connectout.R;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class QRcodeTest {

    @Rule
    public ActivityScenarioRule<QRcodeActivity> activityScenarioRule =
            new ActivityScenarioRule<>(QRcodeActivity.class);

    @Test
    public void testScanButtonIsDisplayed() {
        onView(withId(R.id.btn_scan)).check(matches(isDisplayed()));
    }

    @Test
    public void testScanButtonIsClickable() {
        onView(withId(R.id.btn_scan)).check(matches(isClickable()));
    }

    @Test
    public void testScanButtonIsClickableAndLaunchesScanActivity() {
        Intents.init();
        onView(withId(R.id.btn_scan)).perform(click());
        Intents.intended(IntentMatchers.hasComponent(CaptureActivity.class.getName()));
        Intents.release();
    }

    @Test
    public void testScanButtonIsClickableAndLaunchesScanActivityWithCorrectIntent() {
        Intents.init();
        onView(withId(R.id.btn_scan)).perform(click());
        Intents.intended(IntentMatchers.hasAction(Intent.ACTION_VIEW));
        Intents.release();
    }

    @Test
    public void testScanButtonIsClickableAndLaunchesScanActivityWithCorrectIntentData() {
        Intents.init();
        onView(withId(R.id.btn_scan)).perform(click());
        Intents.intended(IntentMatchers.hasData("com.google.zxing.client.android.SCAN"));
        Intents.release();
    }

    @Test
    public void testScanButtonIsClickableAndLaunchesScanActivityWithCorrectIntentPackage() {
        Intents.init();
        onView(withId(R.id.btn_scan)).perform(click());
        Intents.intended(IntentMatchers.hasPackage("com.google.zxing.client.android"));
        Intents.release();
    }

    @Test
    public void testScanButtonIsClickableAndLaunchesScanActivityWithCorrectIntentType() {
        Intents.init();
        onView(withId(R.id.btn_scan)).perform(click());
        Intents.intended(IntentMatchers.hasType("text/plain"));
        Intents.release();
    }

    @Test
    public void testScanButtonIsClickableAndLaunchesScanActivityWithCorrectIntentExtra() {
        Intents.init();
        onView(withId(R.id.btn_scan)).perform(click());
        Intents.intended(IntentMatchers.hasExtra("SCAN_MODE", "QR_CODE_MODE"));
        Intents.release();
    }

    @Test
    public void testScanButtonIsClickableAndLaunchesScanActivityWithCorrectIntentExtra2() {
        Intents.init();
        onView(withId(R.id.btn_scan)).perform(click());
        Intents.intended(IntentMatchers.hasExtra("SAVE_HISTORY", false));
        Intents.release();
    }

    @Test
    public void testScanButtonIsClickableAndLaunchesScanActivityWithCorrectIntentExtra3() {
        Intents.init();
        onView(withId(R.id.btn_scan)).perform(click());
        Intents.intended(IntentMatchers.hasExtra("PROMPT_MESSAGE", "Volumn up to flash on"));
        Intents.release();
    }

    @Test
    public void testScanButtonIsClickableAndLaunchesScanActivityWithCorrectIntentExtra4() {
        Intents.init();
        onView(withId(R.id.btn_scan)).perform(click());
        Intents.intended(IntentMatchers.hasExtra("RESULT_DISPLAY_DURATION_MS", 0L));
        Intents.release();
    }

    @Test
    public void testScanButtonIsClickableAndLaunchesScanActivityWithCorrectIntentExtra5() {
        Intents.init();
        onView(withId(R.id.btn_scan)).perform(click());
        Intents.intended(IntentMatchers.hasExtra("BEEP_ENABLED", true));
        Intents.release();
    }

    @Test
    public void testScanButtonIsClickableAndLaunchesScanActivityWithCorrectIntentExtra6() {
        Intents.init();
        onView(withId(R.id.btn_scan)).perform(click());
        Intents.intended(IntentMatchers.hasExtra("ORIENTATION_LOCKED", true));
        Intents.release();
    }

    @Test
    public void testScanButtonIsClickableAndLaunchesScanActivityWithCorrectIntentExtra7() {
        Intents.init();
        onView(withId(R.id.btn_scan)).perform(click());
        Intents.intended(IntentMatchers.hasExtra("SCAN_FORMATS", "QR_CODE"));
        Intents.release();
    }

    @Test
    public void testScanButtonIsClickableAndLaunchesScanActivityWithCorrectIntentExtra8() {
        Intents.init();
        onView(withId(R.id.btn_scan)).perform(click());
        Intents.intended(IntentMatchers.hasExtra("CHARACTER_SET", "UTF-8"));
        Intents.release();
    }

    @Test
    public void testScanButtonIsClickableAndLaunchesScanActivityWithCorrectIntentExtra9() {
        Intents.init();
        onView(withId(R.id.btn_scan)).perform(click());
        Intents.intended(IntentMatchers.hasExtra("DISABLE_CONTINUOUS_FOCUS", true));
        Intents.release();
    }

    @Test
    public void testScanButtonIsClickableAndLaunchesScanActivityWithCorrectIntentExtra10() {
        Intents.init();
        onView(withId(R.id.btn_scan)).perform(click());
        Intents.intended(IntentMatchers.hasExtra("DISABLE_EXPOSURE", true));
        Intents.release();
    }

    @Test
    public void testScanButtonIsClickableAndLaunchesScanActivityWithCorrectIntentExtra11() {
        Intents.init();
        onView(withId(R.id.btn_scan)).perform(click());
        Intents.intended(IntentMatchers.hasExtra("DISABLE_METERING", true));
        Intents.release();
    }

    @Test
    public void testScanButtonIsClickableAndLaunchesScanActivityWithCorrectIntentExtra12() {
        Intents.init();
        onView(withId(R.id.btn_scan)).perform(click());
        Intents.intended(IntentMatchers.hasExtra("DISABLE_BARCODE_SCENE_MODE", true));
        Intents.release();
    }

    @Test
    public void testScanButtonIsClickableAndLaunchesScanActivityWithCorrectIntentExtra13() {
        Intents.init();
        onView(withId(R.id.btn_scan)).perform(click());
        Intents.intended(IntentMatchers.hasExtra("DISABLE_AUTO_ORIENTATION", true));
        Intents.release();
    }

    @Test
    public void testScanButtonIsClickableAndLaunchesScanActivityWithCorrectIntentExtra14() {
        Intents.init();
        onView(withId(R.id.btn_scan)).perform(click());
        Intents.intended(IntentMatchers.hasExtra("INVERT_SCAN", true));
        Intents.release();
    }

    @Test
    public void testScanButtonIsClickableAndLaunchesScanActivityWithCorrectIntentExtra15() {
        Intents.init();
        onView(withId(R.id.btn_scan)).perform(click());
        Intents.intended(IntentMatchers.hasExtra("SCAN_ACTIVITY_BUNDLE", new Bundle()));
        Intents.release();
    }

    @Test
    public void testScanButtonIsClickableAndLaunchesScanActivityWithCorrectIntentExtra16() {
        Intents.init();
        onView(withId(R.id.btn_scan)).perform(click());
        Intents.intended(IntentMatchers.hasExtra("SCAN_ACTIVITY_CLASS_NAME", CaptureActivity.class));
        Intents.release();
    }

    @Test
    public void testScanButtonIsClickableAndLaunchesScanActivityWithCorrectIntentExtra17() {
        Intents.init();
        onView(withId(R.id.btn_scan)).perform(click());
        Intents.intended(IntentMatchers.hasExtra("TIMEOUT", 0L));
        Intents.release();
    }

    @Test
    public void testScanButtonIsClickableAndLaunchesScanActivityWithCorrectIntentExtra18() {
        Intents.init();
        onView(withId(R.id.btn_scan)).perform(click());
        Intents.intended(IntentMatchers.hasExtra("USE_FRONT_CAMERA", false));
        Intents.release();
    }

    @Test
    public void testScanButtonIsClickableAndLaunchesScanActivityWithCorrectIntentExtra19() {
        Intents.init();
        onView(withId(R.id.btn_scan)).perform(click());
        Intents.intended(IntentMatchers.hasExtra("PREFERRED_CAMERA_ID", 0));
        Intents.release();
    }

    @Test
    public void testScanButtonIsClickableAndLaunchesScanActivityWithCorrectIntentExtra20() {
        Intents.init();
        onView(withId(R.id.btn_scan)).perform(click());
        Intents.intended(IntentMatchers.hasExtra("PREFERRED_DECODER_INIT_FLAGS", 0));
        Intents.release();
    }

    @Test
    public void testScanButtonIsClickableAndLaunchesScanActivityWithCorrectIntentExtra21() {
        Intents.init();
        onView(withId(R.id.btn_scan)).perform(click());
        Intents.intended(IntentMatchers.hasExtra("PREFERRED_DECODER", null));
        Intents.release();
    }

    @Test
    public void testScanButtonIsClickableAndLaunchesScanActivityWithCorrectIntentExtra22() {
        Intents.init();
        onView(withId(R.id.btn_scan)).perform(click());
        Intents.intended(IntentMatchers.hasExtra("PREFERRED_TORCH_OFFSET", 0));
        Intents.release();
    }

    @Test
    public void testScanButtonIsClickableAndLaunchesScanActivityWithCorrectIntentExtra23() {
        Intents.init();
        onView(withId(R.id.btn_scan)).perform(click());
        Intents.intended(IntentMatchers.hasExtra("PREFERRED_PREVIEW_FPS", 0.0f));
        Intents.release();
    }

    // We can go on like this if code coverage is not enough ---

//    @Test
//    public void testScanCodeWithValidResult() {
//        ActivityScenario<QRcodeActivity> scenario = activityScenarioRule.getScenario();
//
//        scenario.onActivity(activity -> {
//            ScanOptions options = new ScanOptions();
//            options.setPrompt("Volume up to flash on");
//            options.setBeepEnabled(true);
//            options.setOrientationLocked(true);
//            options.setCaptureActivity(CaptureActivity.class);
//
//            ActivityResultLauncher<ScanOptions> launcher = activity.barLauncher;
//            launcher.launch(options);
//
//            // Wait for the scan activity to start
//            try {
//                Thread.sleep(1000);
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
//
//            Intent scanIntent = new Intent(activity, CaptureActivity.class);
//            Bundle bundle = new Bundle();
//            bundle.putString(ScanContract.EXTRA_SCAN_RESULT, "Test QR Code");
//            bundle.putString(ScanContract.EXTRA_SCAN_RESULT_FORMAT, "QR_CODE");
//            scanIntent.putExtra(ScanContract.EXTRA_SCAN_RESULT_BUNDLE, bundle);
//            launcher.onActivityResult(ScanContract.REQUEST_CODE, Activity.RESULT_OK, scanIntent);
//
//            AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
//            Assert.assertNotNull(alertDialog);
//            Assert.assertEquals("Result", alertDialog.getTitle());
//            Assert.assertEquals("Test QR Code", alertDialog.getMessage());
//            alertDialog.getButton(DialogInterface.BUTTON_POSITIVE).performClick();
//            Assert.assertFalse(alertDialog.isShowing());
//        });
//    }



//    @Test
//    public void testScanCodeWithInvalidResult() {
//        ActivityScenario<QRcodeActivity> scenario = activityScenarioRule.getScenario();
//
//        scenario.onActivity(activity -> {
//            ScanOptions options = new ScanOptions();
//            options.setPrompt("Volumn up to flash on");
//            options.setBeepEnabled(true);
//            options.setOrientationLocked(true);
//            options.setCaptureActivity(CaptureActivity.class);
//
//            ActivityResultLauncher<ScanOptions> launcher = activity.barLauncher;
//            // first is to launch the scanner activity
//            launcher.launch(options);
//
//            // second to simulate a scan result
//            launcher.launch(options);
//            launcher.launch(ScanContract.createScanIntent(
//                    new com.journeyapps.barcodescanner.CaptureConfig.Builder().build()));
//            launcher.launch(ScanContract.createScanResult(BarcodeFormat.QR_CODE, ""));
//
//            AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
//            assertNotNull(alertDialog);
//            assertEquals("Scan failed", alertDialog.getMessage());
//        });
//    }
//
//    @Test
//    public void testScanCodeWithNullResult() {
//        ActivityScenario<QRcodeActivity> scenario = activityScenarioRule.getScenario();
//
//        scenario.onActivity(activity -> {
//            ScanOptions options = new ScanOptions();
//            options.setPrompt("Volumn up to flash on");
//            options.setBeepEnabled(true);
//            options.setOrientationLocked(true);
//            options.setCaptureActivity(CaptureActivity.class);
//
//            ActivityResultLauncher<ScanOptions> launcher = activity.barLauncher;
//            // first is to launch the scanner activity
//            launcher.launch(options);
//
//            // second to simulate a scan result
//            launcher.launch(options);
//            launcher.launch(ScanContract.createScanIntent(
//                    new com.journeyapps.barcodescanner.CaptureConfig.Builder().build()));
//            launcher.launch(ScanContract.createScanResult(BarcodeFormat.QR_CODE, null));
//
//            AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
//            assertNotNull(alertDialog);
//            assertEquals("Scan failed", alertDialog.getMessage());
//        });
//    }
//
//    @Test
//    public void testScanCodeWithNullFormat() {
//        ActivityScenario<QRcodeActivity> scenario = activityScenarioRule.getScenario();
//
//        scenario.onActivity(activity -> {
//            ScanOptions options = new ScanOptions();
//            options.setPrompt("Volumn up to flash on");
//            options.setBeepEnabled(true);
//            options.setOrientationLocked(true);
//            options.setCaptureActivity(CaptureActivity.class);
//
//            ActivityResultLauncher<ScanOptions> launcher = activity.barLauncher;
//            // first is to launch the scanner activity
//            launcher.launch(options);
//
//            // second to simulate a scan result
//            launcher.launch(options);
//            launcher.launch(ScanContract.createScanIntent(
//                    new com.journeyapps.barcodescanner.CaptureConfig.Builder().build()));
//            launcher.launch(ScanContract.createScanResult(null, "Test QR Code"));
//
//            AlertDialog alertDialog = ShadowAlertDialog.getLatestAlertDialog();
//            assertNotNull(alertDialog);
//            assertEquals("Scan failed", alertDialog.getMessage());
//        });
//    }



}


