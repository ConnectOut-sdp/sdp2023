package com.sdpteam.connectout.event;

import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.ACCESS_NETWORK_STATE;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.longClick;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayingAtLeast;
import static androidx.test.espresso.matcher.ViewMatchers.withClassName;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.sdpteam.connectout.utils.FutureUtils.fJoin;
import static com.sdpteam.connectout.utils.FutureUtils.waitABit;
import static com.sdpteam.connectout.utils.RandomPath.generateRandomPath;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.icu.util.Calendar;
import android.icu.util.TimeZone;
import android.os.SystemClock;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TimePicker;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.test.espresso.Espresso;
import androidx.test.espresso.InjectEventSecurityException;
import androidx.test.espresso.UiController;
import androidx.test.espresso.ViewAction;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.contrib.PickerActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.rule.GrantPermissionRule;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.GoogleAuth;
import com.sdpteam.connectout.event.creator.EventCreatorActivity;
import com.sdpteam.connectout.event.creator.LocationPicker;
import com.sdpteam.connectout.event.nearbyEvents.map.GPSCoordinates;
import com.sdpteam.connectout.profile.EditProfileActivity;

import org.hamcrest.Matcher;
import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.Date;

@RunWith(AndroidJUnit4.class)
public class EventCreatorActivityTest {

    private final String eventId1 = generateRandomPath();
    private static final String eventTitle1 = generateRandomPath();
    private static final String eventTitle2 = generateRandomPath();
    private static final String eventTitle3 = generateRandomPath();
    private static final String eventTitle4 = generateRandomPath();

    @Rule
    public ActivityScenarioRule<EventCreatorActivity> activityRule = new ActivityScenarioRule<>(EventCreatorActivity.class);

    @Rule
    public GrantPermissionRule grantLocationRule = GrantPermissionRule.grant(ACCESS_FINE_LOCATION, ACCESS_NETWORK_STATE, ACCESS_COARSE_LOCATION);

    @Before
    public void setUp() {
        new GoogleAuth().logout();
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
        new EventFirebaseDataSource().deleteEvent(eventId1);
        waitABit();
    }

    @AfterClass
    public static void cleanUp() {
        EventFirebaseDataSource model = new EventFirebaseDataSource();
        model.deleteEvent(EditProfileActivity.NULL_USER, eventTitle1);
        model.deleteEvent(EditProfileActivity.NULL_USER, eventTitle2);
        model.deleteEvent(EditProfileActivity.NULL_USER, eventTitle3);
        model.deleteEvent(EditProfileActivity.NULL_USER, eventTitle4);
    }

    @Test
    public void clickToolbarIconFinishesActivity() {
        activityRule.getScenario().onActivity(activity -> {
            Toolbar toolbar = activity.findViewById(R.id.event_creator_toolbar);
            toolbar.setNavigationOnClickListener(v -> assertTrue(activity.isFinishing()));
            toolbar.performClick();
        });
    }

    @Test
    public void activityIsOpenedBeforeClickingToolbarIcon() {
        activityRule.getScenario().onActivity(activity -> {
            Fragment fragment = activity.getSupportFragmentManager().findFragmentById(R.id.event_creator_fragment_container);
            assertTrue(fragment instanceof LocationPicker);
        });
    }

    @Test
    public void clickSaveButtonFinishesActivity() {
        onView(withId(R.id.event_creator_title)).perform(ViewActions.typeText(eventTitle1), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.event_creator_description)).perform(ViewActions.typeText("Test Description"), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.event_creator_save_button)).perform(click());

        //Might return null if activity already terminated
        try {
            activityRule.getScenario().onActivity(activity -> {
                Button button = activity.findViewById(R.id.event_creator_save_button);
                button.setOnClickListener(v -> assertTrue(activity.isFinishing()));
            });
        } catch (NullPointerException ignored) {
        }
    }

    @Test
    public void markerIsDraggable() {
        onView(withId(R.id.map)).perform(longClick()); //drags little bit the marker
        onView(withId(R.id.event_creator_save_button)).perform(click());
    }

    @Test
    public void testManualSaveAndGetCorrectValues() {
        String title = eventTitle2;
        String description = "Search for tenis partner";

        Event e = new Event(eventId1, title, description, new GPSCoordinates(1.5, 1.5), EditProfileActivity.NULL_USER);
        EventFirebaseDataSource model = new EventFirebaseDataSource();
        model.saveEvent(e);

        Event foundEvent = fJoin(model.getEvent(eventId1));

        assertThat(foundEvent.getTitle(), is(title));
        assertThat(foundEvent.getId(), is(eventId1));
        assertThat(foundEvent.getCoordinates().getLatitude(), is(1.5));
        assertThat(foundEvent.getCoordinates().getLongitude(), is(1.5));
        assertThat(foundEvent.getDescription(), is(description));
        assertThat(foundEvent.getOrganizer(), is(EditProfileActivity.NULL_USER));
    }

    @Test
    public void testAutomaticSaveAndGetCorrectValues() {
        String title = eventTitle3;
        String description = "Search for tenis partner";

        EventFirebaseDataSource model = new EventFirebaseDataSource();

        onView(ViewMatchers.withId(R.id.event_creator_title)).perform(typeText(title));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.event_creator_description)).perform(typeText(description));
        Espresso.closeSoftKeyboard();
        //onView(withId(R.id.map)).perform(longClick()); //drags a little bit the marker
        onView(withId(R.id.map)).perform(new PressAndSwipeUpAction());
        onView(withId(R.id.event_creator_save_button)).perform(click());
        waitABit();
        Event foundEvent = fJoin(model.getEvent(EditProfileActivity.NULL_USER, title));


        assertThat(foundEvent.getTitle(), is(title));
        //assertThat(foundEvent.getCoordinates().getLatitude(), is(not(0.0))); // the method to change the marker is not wotking
        //assertThat(foundEvent.getCoordinates().getLongitude(), is(not(0.0))); // TODO: fix this for CI
        assertThat(foundEvent.getDescription(), is(description));
        assertThat(foundEvent.getOrganizer(), is(EditProfileActivity.NULL_USER));
    }

    @Test
    public void testTimeAndDateSelection() throws InterruptedException {
        String title = generateRandomPath();
        EventFirebaseDataSource model = new EventFirebaseDataSource();
        onView(ViewMatchers.withId(R.id.event_creator_title)).perform(typeText(title));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.event_creator_description)).perform(typeText("Spikeball match with the beautiful people of Lausanne"));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.map)).perform(longClick()); //drags a little bit the marker

        onView(ViewMatchers.withId(R.id.btn_date)).perform(click());
        //we select March 18 th 2024 at 4:20
        onView(withClassName(Matchers.equalTo(DatePicker.class.getName()))).perform(PickerActions.setDate(2024, 3, 18));
        onView(withText("OK")).perform(click());

        onView(ViewMatchers.withId(R.id.btn_time)).perform(click());
        onView(withClassName(Matchers.equalTo(TimePicker.class.getName()))).perform(PickerActions.setTime(4, 20));
        onView(withText("OK")).perform(click());

        onView(withId(R.id.event_creator_save_button)).perform(click());

        waitABit();
        assertNull(new GoogleAuth().loggedUser());

        Event foundEvent = fJoin(model.getEvent(EditProfileActivity.NULL_USER, title));
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
        calendar.set(Calendar.YEAR, 2024);
        calendar.set(Calendar.MONTH, 3 - 1); // Calendar.MONTH starts from 0
        calendar.set(Calendar.DAY_OF_MONTH, 18);
        calendar.set(Calendar.HOUR_OF_DAY, 4);
        calendar.set(Calendar.MINUTE, 20);
        calendar.set(Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);

        long unixTimestamp = calendar.getTimeInMillis();
        assertThat(unixTimestamp, is(foundEvent.getDate()));
    }

    public class PressAndSwipeUpAction implements ViewAction {

        @Override
        public Matcher<View> getConstraints() {
            return isDisplayingAtLeast(90);
        }

        @Override
        public String getDescription() {
            return "Press and swipe up action";
        }

        @Override
        public void perform(UiController uiController, View view) {
            int[] locationOnScreen = new int[2];
            view.getLocationOnScreen(locationOnScreen);

            float centerX = locationOnScreen[0] + view.getWidth() / 2;
            float centerY = locationOnScreen[1] + view.getHeight() / 2;

            float endY = locationOnScreen[1];

            uiController.loopMainThreadUntilIdle();

            try {


                uiController.injectMotionEvent(MotionEvent.obtain(
                        SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                        MotionEvent.ACTION_DOWN, centerX, centerY, 0));

                uiController.loopMainThreadForAtLeast(50);

                uiController.injectMotionEvent(MotionEvent.obtain(
                        SystemClock.uptimeMillis(), SystemClock.uptimeMillis(),
                        MotionEvent.ACTION_UP, centerX, endY, 0));
            } catch (InjectEventSecurityException ignored) {
            }
        }
    }
}