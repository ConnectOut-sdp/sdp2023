package com.sdpteam.connectout.profile;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;
import static com.sdpteam.connectout.profile.ReportProfileActivity.REPORTED_UID;
import static com.sdpteam.connectout.utils.FutureUtils.fJoin;
import static com.sdpteam.connectout.utils.FutureUtils.waitABit;
import static com.sdpteam.connectout.utils.RandomPath.generateRandomPath;
import static org.junit.Assert.assertEquals;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.AuthenticatedUser;
import com.sdpteam.connectout.authentication.GoogleAuth;

import org.junit.After;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ReportProfileActivityTest {

    public static final String reportedUid = generateRandomPath();
    private final ReportFirebaseDataSource model = new ReportFirebaseDataSource();

    static Intent intent;

    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), ReportProfileActivity.class);
        intent.putExtra(REPORTED_UID, reportedUid);
    }

    @Rule
    public ActivityScenarioRule<ReportProfileActivity> testRule = new ActivityScenarioRule<>(intent);

    @After
    public void cleanUp() {
        model.deleteReport(reportedUid);
    }

    @Test
    public void testDisplayedElements() {
        onView(withId(R.id.ReportText)).check(matches(isDisplayed()));
        onView(withId(R.id.submitReportButton)).check(matches(isDisplayed()));
        onView(withId(R.id.reportTextView)).check(matches(isDisplayed()));
    }

    @Test
    public void testReport() {
        onView(withId(R.id.ReportText)).perform(typeText("test report"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.submitReportButton)).perform(click());
        waitABit();

        AuthenticatedUser au = new GoogleAuth().loggedUser();
        String reporterUid = (au == null) ? NULL_USER : au.uid;
        assertEquals("test report", fJoin(model.fetchReport(reportedUid, reporterUid)));
    }
}
