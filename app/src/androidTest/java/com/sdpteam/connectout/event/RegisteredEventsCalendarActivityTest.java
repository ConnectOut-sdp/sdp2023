package com.sdpteam.connectout.event;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;

import static org.hamcrest.CoreMatchers.allOf;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.anything;
import static org.hamcrest.Matchers.is;

import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.chat.ChatActivity;
import com.sdpteam.connectout.event.nearbyEvents.map.GPSCoordinates;
import com.sdpteam.connectout.event.viewer.EventActivity;
import com.sdpteam.connectout.event.viewer.RegisteredEventsCalendarActivity;
import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileFirebaseDataSource;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.Date;

@RunWith(AndroidJUnit4.class)
public class RegisteredEventsCalendarActivityTest {

    @Rule
    public ActivityScenarioRule<RegisteredEventsCalendarActivity> activityRule = new ActivityScenarioRule<>(RegisteredEventsCalendarActivity.class);
    @Before
    public void setUp() {
        //Intents.init();
        activityRule.getScenario().onActivity(activity -> {
            Event e1 = new Event("test1", "t1", "empty", new GPSCoordinates(0,0),"organizerId", new ArrayList<>(), new Date(2023-1900, 4, 11, 13, 33).getTime());
            Event e2 = new Event("test2", "t2", "empty", new GPSCoordinates(0,0),"organizerId", new ArrayList<>(), new Date(2023-1900, 4, 10, 13, 40).getTime());
            Event e3 = new Event("test3", "t3", "empty", new GPSCoordinates(0,0),"organizerId", new ArrayList<>(), new Date(2023-1900, 4, 12, 13, 34).getTime());
            Event e4 = new Event("test4", "t4", "empty", new GPSCoordinates(0,0),"organizerId", new ArrayList<>(), new Date(2023-1900, 4, 10, 13, 34).getTime());
            //order in time is e4, e2, e1 then e3

            EventFirebaseDataSource eventFirebase = new EventFirebaseDataSource();
            eventFirebase.saveEvent(e1);
            eventFirebase.saveEvent(e2);
            eventFirebase.saveEvent(e3);
            eventFirebase.saveEvent(e4);

            ArrayList<Profile.CalendarEvent> calendarEvents = new ArrayList<>();
            /*calendarEvents.add(new Profile.CalendarEvent(e1.getId(), e1.getTitle(), e1.getDate()));
            calendarEvents.add(new Profile.CalendarEvent(e2.getId(), e2.getTitle(), e2.getDate()));
            calendarEvents.add(new Profile.CalendarEvent(e3.getId(), e3.getTitle(), e3.getDate()));
            calendarEvents.add(new Profile.CalendarEvent(e4.getId(), e4.getTitle(), e4.getDate()));*/
            Profile p = new Profile(NULL_USER, "user_test_name", "fake_email", "fake_bio", Profile.Gender.MALE, 0, 0,  calendarEvents);
            ProfileFirebaseDataSource profileFirebase = new ProfileFirebaseDataSource();
            profileFirebase.saveProfile(p);
        });
    }

    @After
    public void tearDown() {
        //Intents.release();
        //TODO Delete added Events and Profile
        //TODO test registerToEvent from the datasource
    }

    @Test
    public void testCalendarViewOrdering(){
        onView(ViewMatchers.withId(R.id.activity_registered_events_calendar)).check(matches(isDisplayed()));
        Profile p = new ProfileFirebaseDataSource().fetchProfile(NULL_USER).join();
        assertThat(p.getRegisteredEvents().size(), is(4));
        checkCalendarListViewValue(0, "t4");
        checkCalendarListViewValue(1, "t2");
        checkCalendarListViewValue(2, "t1");
        checkCalendarListViewValue(3, "t3");
    }

    /*
    @Test
    public void clickingOnCalendarEventOpensEvent(){
        onView(withId(R.id.nameofcalendarentry)).perform(clickOnASpecificElement());
        intended(allOf(IntentMatchers.hasComponent(EventActivity.class.getName()),
                IntentMatchers.hasExtra("key", idOfClickedEvent)
        ));
        onView(withId(R.id.event_title)).check(matches(withText(clickedEvent.getTitle())))
    }*/

    public void checkCalendarListViewValue(int position, String title) {
        onData(anything()).inAdapterView(withId(R.id.activity_registered_events_calendar)).atPosition(position).
                onChildView(withId(R.id.registered_event_title)).
                check(matches(withText(title)));
    }
}
