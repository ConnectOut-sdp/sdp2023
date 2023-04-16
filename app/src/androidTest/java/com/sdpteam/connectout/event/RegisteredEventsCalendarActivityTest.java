package com.sdpteam.connectout.event;

import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdpteam.connectout.event.nearbyEvents.map.GPSCoordinates;
import com.sdpteam.connectout.profile.Profile;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class RegisteredEventsCalendarActivityTest {
    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void testCalendarView(){

        //TODO give dates different than 0 to be able to order the values
        Event e1 = new Event("test1", "t1", "empty", new GPSCoordinates(0,0),"organizerId", new ArrayList<>(), 0);
        Event e2 = new Event("test2", "t2", "empty", new GPSCoordinates(0,0),"organizerId", new ArrayList<>(), 0);
        Event e3 = new Event("test3", "t3", "empty", new GPSCoordinates(0,0),"organizerId", new ArrayList<>(), 0);
        Event e4 = new Event("test4", "t4", "empty", new GPSCoordinates(0,0),"organizerId", new ArrayList<>(), 0);

        ArrayList<Profile.CalendarEvent> calendarEvents = new ArrayList<>();
        //calendar.add(new Profile.CalendarEvent());
        Profile p = new Profile("uid", "user_test_name", "fake_email", "fake_bio", Profile.Gender.MALE, 0, 0,  calendarEvents);
    }
}
