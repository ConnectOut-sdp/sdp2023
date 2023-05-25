package com.sdpteam.connectout.QrCode;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class QRcodeActivityTest {

    @Rule
    public ActivityScenarioRule<QRcodeActivity> testRule = new ActivityScenarioRule<>(QRcodeActivity.class);


    @Test
    public void testHandleScanResultForProfile() {
        testRule.getScenario().onActivity(activity ->
        {
            // Given
            String profileResult = "profile/12345";

            Boolean bool = activity.handleScanResult(profileResult);
            assertTrue(bool);
        });
    }

    @Test
    public void testHandleScanResultForEvent() {
        testRule.getScenario().onActivity(activity ->
        {
            // Given
            String eventResult = "event/54321";
            assertTrue(activity.handleScanResult(eventResult));
        });
    }

    @Test
    public void testHandleScanResultForInvalidType() {
        testRule.getScenario().onActivity(activity ->
        {

            // Given
            String invalidTypeResult = "invalidType/12345";
            assertFalse(activity.handleScanResult(invalidTypeResult));
        });
    }

    @Test
    public void testHandleScanResultForInvalidFormat() {
        testRule.getScenario().onActivity(activity ->
        {
            // Given
            String invalidFormatResult = "invalidFormat";
            assertFalse(activity.handleScanResult(invalidFormatResult));
        });
    }

    /*@Test
    public void testHandleScanResultForNullInput() {
        testRule.getScenario().onActivity(activity ->
        {
            // Given
            String invalidFormatResult = null;
            assertFalse(activity.handleScanResult(invalidFormatResult));
        });
    }*/



}




