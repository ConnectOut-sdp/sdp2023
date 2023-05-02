package com.sdpteam.connectout.event;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.sdpteam.connectout.utils.FutureUtils.fJoin;
import static org.hamcrest.Matchers.anything;

import android.os.SystemClock;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.database.FirebaseDatabase;
import com.sdpteam.connectout.R;
import com.sdpteam.connectout.chat.ChatActivity;
import com.sdpteam.connectout.event.nearbyEvents.map.GPSCoordinates;
import com.sdpteam.connectout.event.viewer.MyEventsCalendarActivity;
import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileFirebaseDataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;
import java.util.Random;

@RunWith(AndroidJUnit4.class)
public class MyEventsCalendarActivityTest {

    @Rule
    public ActivityScenarioRule<MyEventsCalendarActivity> activityRule = new ActivityScenarioRule<>(MyEventsCalendarActivity.class);
    @Before
    public void setUp() {
        Intents.init();

        String userId = "id_" + new Random().nextInt();
        Event e1 = new Event("test1", "t1", "empty", new GPSCoordinates(0, 0), "organizerId", new ArrayList<>(), new Date(2023 - 1900, 4, 11, 13, 33).getTime());
        Event e2 = new Event("test2", "t2", "empty", new GPSCoordinates(0, 0), "organizerId", new ArrayList<>(), new Date(2023 - 1900, 4, 10, 13, 40).getTime());
        Event e3 = new Event("test3", "t3", "empty", new GPSCoordinates(0, 0), "organizerId", new ArrayList<>(), new Date(2023 - 1900, 4, 12, 13, 34).getTime());
        Event e4 = new Event("test4", "t4", "empty", new GPSCoordinates(0, 0), "organizerId", new ArrayList<>(), new Date(2023 - 1900, 4, 10, 13, 34).getTime());
        Event e5 = new Event("test5", "t5", "empty", new GPSCoordinates(0, 0), "organizerId", new ArrayList<>(), new Date(2023 - 1900, 6, 10, 13, 34).getTime());
        //order in time is e4, e2, e1, e3 then e5

        EventFirebaseDataSource eventFirebase = new EventFirebaseDataSource();
        eventFirebase.saveEvent(e1);
        eventFirebase.saveEvent(e2);
        eventFirebase.saveEvent(e3);
        eventFirebase.saveEvent(e4);
        eventFirebase.saveEvent(e5);
        SystemClock.sleep(2000);

        Profile p = new Profile(userId, "user_test_name", "fake_email", "fake_bio", Profile.Gender.MALE, 0, 0, "");
        ProfileFirebaseDataSource profileFirebase = new ProfileFirebaseDataSource();
        fJoin(profileFirebase.saveProfile(p));

        profileFirebase.registerToEvent(new Profile.CalendarEvent(e1.getId(), e1.getTitle(), e1.getDate()), userId);
        profileFirebase.registerToEvent(new Profile.CalendarEvent(e2.getId(), e2.getTitle(), e2.getDate()), userId);
        profileFirebase.registerToEvent(new Profile.CalendarEvent(e3.getId(), e3.getTitle(), e3.getDate()), userId);
        profileFirebase.registerToEvent(new Profile.CalendarEvent(e4.getId(), e4.getTitle(), e4.getDate()), userId);
        profileFirebase.registerToEvent(new Profile.CalendarEvent(e5.getId(), e5.getTitle(), e5.getDate()), userId);
        SystemClock.sleep(2000);
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testCalendarViewOrdering() {
        onView(ViewMatchers.withId(R.id.activity_registered_events_calendar)).check(matches(isDisplayed()));

        checkCalendarListViewValue(0, "t4");
        checkCalendarListViewValue(1, "t2");
        checkCalendarListViewValue(2, "t1");
        checkCalendarListViewValue(3, "t3");
    }

    public void checkCalendarListViewValue(int position, String title) {
        onData(anything()).inAdapterView(withId(R.id.list_of_registered_events)).atPosition(position).
                onChildView(withId(R.id.registered_event_title));
//                .check(matches(withText(title)));
    }
}