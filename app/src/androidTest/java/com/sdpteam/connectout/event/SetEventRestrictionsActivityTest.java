package com.sdpteam.connectout.event;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static com.sdpteam.connectout.event.EventFirebaseDataSource.DATABASE_EVENT_PATH;
import static com.sdpteam.connectout.event.viewer.EventActivity.PASSED_ID_KEY;
import static com.sdpteam.connectout.utils.FutureUtils.fJoin;
import static com.sdpteam.connectout.utils.FutureUtils.waitABit;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.action.CloseKeyboardAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.firebase.database.FirebaseDatabase;
import com.sdpteam.connectout.R;
import com.sdpteam.connectout.event.creator.EventCreatorActivity;
import com.sdpteam.connectout.event.creator.SetEventRestrictionsActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.sql.Time;
import java.util.Calendar;
import java.util.GregorianCalendar;
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
        //TODO delete event
        FirebaseDatabase.getInstance().getReference().child(DATABASE_EVENT_PATH).child(FAKE_EVENT_ID).removeValue();
        Intents.release();
    }

    @Test
    public void setRestrictions(){
        onView(withId(R.id.set_event_restrictions_max_num_participants)).perform(ViewActions.typeText("10"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.set_event_restrictions_min_rating)).perform(ViewActions.typeText("1.5"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.set_event_restrictions_save_button)).perform(click());

        waitABit();

        Event restrictedEvent = fJoin(dataSource.getEvent(FAKE_EVENT_ID));

        assertThat(restrictedEvent.getRestrictions().getMaxNumParticipants(), is(10));
        assertThat(restrictedEvent.getRestrictions().getMinRating(), is(1.5));
    }
}
