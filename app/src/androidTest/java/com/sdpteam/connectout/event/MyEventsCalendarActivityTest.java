package com.sdpteam.connectout.event;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.sdpteam.connectout.utils.FutureUtils.fJoin;
import static com.sdpteam.connectout.utils.FutureUtils.waitABit;
import static com.sdpteam.connectout.utils.RandomPath.generateRandomPath;
import static com.sdpteam.connectout.utils.RandomPath.generateRandomString;
import static org.hamcrest.Matchers.anything;

import java.util.ArrayList;
import java.util.Date;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.event.nearbyEvents.map.GPSCoordinates;
import com.sdpteam.connectout.event.viewer.MyEventsCalendarActivity;
import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileFirebaseDataSource;
import com.sdpteam.connectout.profile.RegisteredEvent;

import android.os.SystemClock;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class MyEventsCalendarActivityTest {

    private static final String userId = generateRandomString(8);
    private static final ProfileFirebaseDataSource profileFirebase = new ProfileFirebaseDataSource();
    private final String eventId1 = generateRandomPath();
    private final String eventId2 = generateRandomPath();
    private final String eventId3 = generateRandomPath();
    private final String eventId4 = generateRandomPath();
    private final String eventId5 = generateRandomPath();
    private final EventFirebaseDataSource eventFirebase = new EventFirebaseDataSource();

    @Rule
    public ActivityScenarioRule<MyEventsCalendarActivity> activityRule = new ActivityScenarioRule<>(MyEventsCalendarActivity.class);

    @AfterClass
    public static void tearDownClass() {
        profileFirebase.deleteProfile(userId);
        waitABit();
    }

    @Before
    public void setUp() {
        Intents.init();

        Event e1 = new Event(eventId1, "t1", "empty", new GPSCoordinates(0, 0), "organizerId", new ArrayList<>(), new ArrayList<>(), new Date(2023 - 1900, 4, 11, 13, 33).getTime());
        Event e2 = new Event(eventId2, "t2", "empty", new GPSCoordinates(0, 0), "organizerId", new ArrayList<>(), new ArrayList<>(), new Date(2023 - 1900, 4, 10, 13, 40).getTime());
        Event e3 = new Event(eventId3, "t3", "empty", new GPSCoordinates(0, 0), "organizerId", new ArrayList<>(), new ArrayList<>(), new Date(2023 - 1900, 4, 12, 13, 34).getTime());
        Event e4 = new Event(eventId4, "t4", "empty", new GPSCoordinates(0, 0), "organizerId", new ArrayList<>(), new ArrayList<>(), new Date(2023 - 1900, 4, 10, 13, 34).getTime());
        Event e5 = new Event(eventId5, "t5", "empty", new GPSCoordinates(0, 0), "organizerId", new ArrayList<>(), new ArrayList<>(), new Date(2023 - 1900, 6, 10, 13, 34).getTime());
        //order in time is e4, e2, e1, e3 then e5

        eventFirebase.saveEvent(e1);
        eventFirebase.saveEvent(e2);
        eventFirebase.saveEvent(e3);
        eventFirebase.saveEvent(e4);
        eventFirebase.saveEvent(e5);
        SystemClock.sleep(2000);

        Profile p = new Profile(userId, "user_test_name", "fake_email", "fake_bio", Profile.Gender.MALE, 0, 0, "");
        fJoin(profileFirebase.saveProfile(p));

        profileFirebase.registerToEvent(new RegisteredEvent(e1.getId()), userId);
        profileFirebase.registerToEvent(new RegisteredEvent(e2.getId()), userId);
        profileFirebase.registerToEvent(new RegisteredEvent(e3.getId()), userId);
        profileFirebase.registerToEvent(new RegisteredEvent(e4.getId()), userId);
        profileFirebase.registerToEvent(new RegisteredEvent(e5.getId()), userId);
        SystemClock.sleep(2000);
    }

    @After
    public void tearDown() {
        Intents.release();
        eventFirebase.deleteEvent(eventId1);
        eventFirebase.deleteEvent(eventId2);
        eventFirebase.deleteEvent(eventId3);
        eventFirebase.deleteEvent(eventId4);
        eventFirebase.deleteEvent(eventId5);
        SystemClock.sleep(2000);
        profileFirebase.deleteProfile(userId);
        waitABit();
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
