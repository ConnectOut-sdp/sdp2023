package com.sdpteam.connectout.profile;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;
import static org.junit.Assert.assertEquals;

import android.content.Intent;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;
import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.AuthenticatedUser;
import com.sdpteam.connectout.authentication.GoogleAuth;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ReportProfileActivityTest {

    public static String reportedUid = "testuid";
    private final ReportModel model = new ReportModel();

    static Intent intent;

    static {
        intent = new Intent(ApplicationProvider.getApplicationContext(), ReportProfileActivity.class);
        intent.putExtra("reportUid", reportedUid);
    }

    @Rule
    public ActivityScenarioRule<ReportProfileActivity> testRule = new ActivityScenarioRule<>(intent);

    @Test
    public void testDisplayedElements() {
        onView(withId(R.id.ReportText)).check(matches(isDisplayed()));
        onView(withId(R.id.submitReportButton)).check(matches(isDisplayed()));
        onView(withId(R.id.reportTextView)).check(matches(isDisplayed()));
    }

    @Test
    public void testReport() {
        model.deleteProfile(reportedUid);
        onView(withId(R.id.ReportText)).perform(typeText("test report"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.submitReportButton)).perform(click());
        addListener();
    }

    private void addListener() {
        AuthenticatedUser au = new GoogleAuth().loggedUser();
        String reporterUid = (au == null) ? NULL_USER : au.uid;
        model.getDatabaseReference().child("Report").child(reportedUid).child(reporterUid).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                assertEquals("test report" , dataSnapshot.getValue(String.class));
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(this.getClass().toString(), "Failed to read value.", databaseError.toException());
            }
        });
    }
}
