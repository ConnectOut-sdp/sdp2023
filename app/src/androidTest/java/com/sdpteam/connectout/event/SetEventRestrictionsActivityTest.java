package com.sdpteam.connectout.event;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.sdpteam.connectout.event.EventFirebaseDataSource.DATABASE_EVENT_PATH;
import static com.sdpteam.connectout.event.viewer.EventActivity.PASSED_ID_KEY;
import static com.sdpteam.connectout.utils.FutureUtils.fJoin;
import static com.sdpteam.connectout.utils.FutureUtils.waitABit;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import android.content.Intent;
import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.database.FirebaseDatabase;
import com.sdpteam.connectout.R;
import com.sdpteam.connectout.event.creator.SetEventRestrictionsActivity;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Random;

@RunWith(AndroidJUnit4.class)
public class SetEventRestrictionsActivityTest {
    private final static String FAKE_EVENT_ID = "SetEventRestrictionActivityTest_" + new Random().nextInt();

    private static EventFirebaseDataSource dataSource = new EventFirebaseDataSource();
    @Rule
    public ActivityScenarioRule<SetEventRestrictionsActivity> activityRule = new ActivityScenarioRule<>(new Intent(ApplicationProvider.getApplicationContext(), SetEventRestrictionsActivity.class).putExtra(PASSED_ID_KEY, FAKE_EVENT_ID));

    @BeforeClass
    public static void setUpBeforeClass() {
        dataSource.saveEvent(new Event(FAKE_EVENT_ID, "TEST TITLE", "TEST DESCRIPTION FOR TEST IN DESCRIPTION OF TEST", null, "TEST ORGANIZER ID"));
        //add fake event id to the intent
        waitABit();
    }

    @Before
    public void setUp(){
        Intents.init();
    }
    @After
    public void tearDown() {
        FirebaseDatabase.getInstance().getReference().child(DATABASE_EVENT_PATH).child(FAKE_EVENT_ID).removeValue();
        Intents.release();
    }

    @Test
    public void setRestrictions(){
        onView(withId(R.id.set_event_restrictions_max_num_participants)).perform(ViewActions.typeText("10"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.set_event_restrictions_min_rating)).perform(ViewActions.typeText("1.5"));
        Espresso.closeSoftKeyboard();

        onView(ViewMatchers.withId(R.id.set_event_restrictions_btn_date)).perform(click());
        //we select March 18 th 2024 at 4:20
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2024, 3, 18));
        onView(withText("OK")).perform(click());

        onView(ViewMatchers.withId(R.id.set_event_restrictions_btn_time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(4, 20));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.set_event_restrictions_save_button)).perform(click());

        waitABit();

        Event restrictedEvent = fJoin(dataSource.getEvent(FAKE_EVENT_ID));

        assertThat(restrictedEvent.getRestrictions().getMaxNumParticipants(), is(10));
        assertThat(restrictedEvent.getRestrictions().getMinRating(), is(1.5));

        Calendar calendar = android.icu.util.Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
        calendar.set(android.icu.util.Calendar.YEAR, 2024);
        calendar.set(android.icu.util.Calendar.MONTH, 3 - 1); // Calendar.MONTH starts from 0
        calendar.set(android.icu.util.Calendar.DAY_OF_MONTH, 18);
        calendar.set(android.icu.util.Calendar.HOUR_OF_DAY, 4);
        calendar.set(android.icu.util.Calendar.MINUTE, 20);
        calendar.set(android.icu.util.Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long unixTimestamp = calendar.getTimeInMillis();
        assertThat(restrictedEvent.getRestrictions().getJoiningDeadline(), is(unixTimestamp));
    }
}
