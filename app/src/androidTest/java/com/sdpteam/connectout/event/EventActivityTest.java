package com.sdpteam.connectout.event;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.sdpteam.connectout.event.viewer.EventActivity.JOIN_EVENT;
import static com.sdpteam.connectout.event.viewer.EventActivity.MAKE_POST;
import static com.sdpteam.connectout.event.viewer.EventActivity.PASSED_ID_KEY;
import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;
import static com.sdpteam.connectout.utils.FutureUtils.fJoin;
import static com.sdpteam.connectout.utils.FutureUtils.waitABit;
import static com.sdpteam.connectout.utils.RandomPath.generateRandomPath;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import android.content.Intent;

import androidx.appcompat.widget.Toolbar;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.fragment.app.Fragment;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdpteam.connectout.QrCode.QRcodeModalActivity;
import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.GoogleAuth;
import com.sdpteam.connectout.event.nearbyEvents.map.GPSCoordinates;
import com.sdpteam.connectout.event.viewer.EventActivity;
import com.sdpteam.connectout.event.viewer.EventMapViewFragment;
import com.sdpteam.connectout.post.model.Post;
import com.sdpteam.connectout.post.model.PostFirebaseDataSource;
import com.sdpteam.connectout.post.view.PostCreatorActivity;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Assert;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class EventActivityTest {
    private final static String EVENT_TITLE_1 = generateRandomPath();
    private final static String EVENT_TITLE_QR = generateRandomPath();

    private final static Event TEST_EVENT = new Event(generateRandomPath(), EVENT_TITLE_1, "descr", new GPSCoordinates(1.2, 1.2), "Bob");

    private final static Event TEST_EVENT_QR = new Event(generateRandomPath(), EVENT_TITLE_QR, "descr", new GPSCoordinates(1.2, 1.2), "Bob");

    private final static String POST_ID = "A_" + generateRandomPath();
    private final static String COMMENT_ID = "A_" + generateRandomPath();
    private final static String PROFILE_ID = "A_" + generateRandomPath();

    private final static Post TEST_POST = new Post(POST_ID, PROFILE_ID, TEST_EVENT.getId(), COMMENT_ID, new ArrayList<>(), 100, Post.PostVisibility.PUBLIC, "title", "desc");
    private final static Post TEST_POST2 = new Post(POST_ID + "1", PROFILE_ID, TEST_EVENT.getId(), COMMENT_ID, new ArrayList<>(), 100, Post.PostVisibility.PUBLIC, "title", "desc");

    @Rule
    public ActivityScenarioRule<EventActivity> activityRule = new ActivityScenarioRule<>(new Intent(ApplicationProvider.getApplicationContext(), EventActivity.class).putExtra(PASSED_ID_KEY,
            TEST_EVENT.getId()));
    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @BeforeClass
    public static void setUpClass() {
        new EventFirebaseDataSource().saveEvent(TEST_EVENT);
        new PostFirebaseDataSource().savePost(TEST_POST);
        new PostFirebaseDataSource().savePost(TEST_POST2);
        waitABit();
    }

    @AfterClass
    public static void tearDownClass() {
        new EventFirebaseDataSource().deleteEvent(TEST_EVENT.getId());
        new EventFirebaseDataSource().deleteEvent(TEST_EVENT_QR.getId());
        new PostFirebaseDataSource().deletePost(TEST_POST.getId());
        new PostFirebaseDataSource().deletePost(TEST_POST2.getId());
    }

    @Before
    public void setUp() {
        new EventFirebaseDataSource().saveEvent(TEST_EVENT);
        waitABit();
        Intents.init();
        new GoogleAuth().logout();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void shareQrCodeButton() {
        onView(withId(R.id.buttonShareEventQrCode)).check(matches(isDisplayed()));
        onView(withId(R.id.buttonShareEventQrCode)).perform(click());
        intended(hasComponent(QRcodeModalActivity.class.getName()));


        // Verify that the intent has the expected key
        intended(hasExtraWithKey("title"));
        intended(hasExtraWithKey("qrCodeData"));
    }

    @Test
    public void fragmentIsCorrectlyAdded() {
        activityRule.getScenario().onActivity(activity -> {
            final Fragment fragment = activity.getSupportFragmentManager().findFragmentById(R.id.event_fragment_container);
            assertTrue(fragment instanceof EventMapViewFragment);
        });
        onView(withId(R.id.map)).perform(ViewActions.scrollTo()).perform(ViewActions.click());
        onView(withId(R.id.refresh_button)).perform(ViewActions.scrollTo()).perform(ViewActions.click());
    }

    @Test
    public void fragmentAddsEventCorrectly() {
        activityRule.getScenario().onActivity(activity -> {
            final Fragment fragment = activity.getSupportFragmentManager().findFragmentById(R.id.event_fragment_container);
            assertTrue(fragment instanceof EventMapViewFragment);
            EventMapViewFragment mapViewFragment = (EventMapViewFragment) fragment;
            mapViewFragment.showEventOnMap(TEST_EVENT);
        });
        onView(withId(R.id.map)).perform(ViewActions.scrollTo()).perform(ViewActions.click());
        onView(withId(R.id.refresh_button)).perform(ViewActions.scrollTo()).perform(ViewActions.click());
    }

    @Test
    public void fragmentDoesNotCrashWithNullMap() {
        activityRule.getScenario().onActivity(activity -> {
            final Fragment fragment = activity.getSupportFragmentManager().findFragmentById(R.id.event_fragment_container);
            assertTrue(fragment instanceof EventMapViewFragment);
            EventMapViewFragment mapViewFragment = (EventMapViewFragment) fragment;
            mapViewFragment.onMapReady(null);
            mapViewFragment.showEventOnMap(TEST_EVENT);
        });
    }

    @Test
    public void consecutiveJoinAndLeaveEventChangesBelongingUser() {
        //join event
        onView(withId(R.id.event_join_button)).perform(ViewActions.scrollTo()).check(matches(withText(JOIN_EVENT)));
        onView(withId(R.id.event_join_button)).perform(ViewActions.scrollTo()).perform(ViewActions.click());
        waitABit();
        onView(withId(R.id.refresh_button)).perform(ViewActions.scrollTo()).perform(ViewActions.click());
        waitABit();
        waitABit();
        waitABit();
        //    onView(withId(R.id.event_join_button)).check(matches(withText(LEAVE_EVENT)));
        Event obtained = fJoin(new EventFirebaseDataSource().getEvent(TEST_EVENT.getId()));
        assertNotNull(obtained);
        assertTrue(obtained.hasJoined(NULL_USER));
        // leave event
        onView(withId(R.id.event_join_button)).perform(ViewActions.scrollTo()).perform(ViewActions.click());
        waitABit();
        waitABit();
        waitABit();
        onView(withId(R.id.refresh_button)).perform(ViewActions.scrollTo()).perform(ViewActions.click());
        waitABit();
        waitABit();
        waitABit();
        onView(withId(R.id.event_join_button)).perform(ViewActions.scrollTo());
        waitABit();
        waitABit();
        waitABit();
        waitABit();
        waitABit();
        onView(withId(R.id.event_join_button)).check(matches(withText(JOIN_EVENT)));
        waitABit();
        obtained = fJoin(new EventFirebaseDataSource().getEvent(TEST_EVENT.getId()));
        assertFalse(obtained.hasJoined(NULL_USER));
    }

    @Test
    public void clickToolbarIconFinishesActivity() {
        activityRule.getScenario().onActivity(activity -> {
            Toolbar toolbar = activity.findViewById(R.id.event_toolbar);
            toolbar.setNavigationOnClickListener(v -> Assert.assertTrue(activity.isFinishing()));
            toolbar.performClick();
        });
    }

    @Test
    public void deleteEventTest() {
        EventFirebaseDataSource model = new EventFirebaseDataSource();
        model.saveEvent(TEST_EVENT_QR);
        waitABit();
        model.deleteEvent(TEST_EVENT_QR.getId());
        waitABit();
        assertNull(fJoin(model.getEvent(TEST_EVENT_QR.getId())));
        model.saveEvent(TEST_EVENT_QR);
        waitABit();
        model.deleteEvent(TEST_EVENT_QR.getId());
        waitABit();
        model.deleteEvent(NULL_USER, EVENT_TITLE_QR);
        assertNull(fJoin(model.getEvent(TEST_EVENT_QR.getId())));
    }

    /*
    @Test
    public void checkingRegistrationIsDisabledPassedDeadline() {
        EventFirebaseDataSource dataSource = new EventFirebaseDataSource();
        Calendar calendar = android.icu.util.Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00"));
        calendar.set(android.icu.util.Calendar.YEAR, 2022);
        calendar.set(android.icu.util.Calendar.MONTH, 3 - 1); // Calendar.MONTH starts from 0
        calendar.set(android.icu.util.Calendar.DAY_OF_MONTH, 18);
        calendar.set(android.icu.util.Calendar.HOUR_OF_DAY, 4);
        calendar.set(android.icu.util.Calendar.MINUTE, 20);
        calendar.set(android.icu.util.Calendar.SECOND, 0);
        calendar.set(Calendar.MILLISECOND, 0);
        long unixTimestamp = calendar.getTimeInMillis();

        dataSource.saveEvent(new Event(FAKE_EVENT_ID, "TEST TITLE", "TEST DESCRIPTION FOR TEST IN DESCRIPTION OF TEST",
                null, "TEST ORGANIZER ID", new ArrayList<>(Arrays.asList("TEST ORGANIZER ID")),
                Long.MAX_VALUE, new Event.EventRestrictions(4, 20, unixTimestamp)));
        waitABit();

        onView(withId(R.id.event_join_button)).perform(ViewActions.click());

        waitABit();

        onView(ViewMatchers.withId(R.id.event_chat_btn)).check(matches(isNotClickable())); // check that the registration didn't occur
        FirebaseDatabase.getInstance().getReference().child(DATABASE_EVENT_PATH).child(FAKE_EVENT_ID).removeValue();
    }*/

    @Test
    public void makePostButtonVisibleWhenCurrentUserIsParticipant() {
        //join event
        onView(withId(R.id.event_join_button)).perform(ViewActions.scrollTo()).check(matches(withText(JOIN_EVENT)));
        onView(withId(R.id.event_join_button)).perform(ViewActions.scrollTo()).perform(ViewActions.click());
        waitABit();
        onView(withId(R.id.refresh_button)).perform(ViewActions.scrollTo()).perform(ViewActions.click());
        waitABit();
        waitABit();
        waitABit();
        onView(withId(R.id.event_make_post_button)).check(matches(isDisplayed()));

        // click make post button check intent is launched
        onView(withId(R.id.event_make_post_button)).perform(ViewActions.scrollTo()).check(matches(withText(MAKE_POST)));
        onView(withId(R.id.event_make_post_button)).perform(ViewActions.scrollTo()).perform(ViewActions.click());
        intended(hasComponent(PostCreatorActivity.class.getName()));
    }
}