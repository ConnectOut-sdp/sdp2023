package com.sdpteam.connectout.drawer;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.contrib.DrawerMatchers.isOpen;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import androidx.core.view.GravityCompat;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdpteam.connectout.QrCode.QRcodeActivity;
import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.GoogleLoginActivity;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class DrawerActivityTest {

    @Rule
    public ActivityScenarioRule<DrawerActivity> activityRule = new ActivityScenarioRule<>(DrawerActivity.class);

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void clickHomeOptionOpensHomeFragment() {
        // Open the drawer
        onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open());

        // Click on the Home menu item
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.menu_home));
        onView(withId(R.id.drawer_fragment_container)).check(matches(isDisplayed()));
        onView(withId(R.id.events_fragment_id)).check(matches(isDisplayed()));

    }

    @Test
    public void clickMyAccountOptionOpenMyAccountFragment() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        // Click on the My Account menu item
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.menu_my_account));
        onView(withId(R.id.drawer_fragment_container)).check(matches(isDisplayed()));
        onView(withId(R.id.profile_fragment_id)).check(matches(isDisplayed()));
    }

    @Test
    public void clickMyEventsOptionOpensMyEventsFragment() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        // Click on the My Events menu item
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.menu_my_events));
        onView(withId(R.id.drawer_fragment_container)).check(matches(isDisplayed()));
        onView(withId(R.id.activity_registered_events_calendar)).check(matches(isDisplayed()));
    }

    @Test
    public void clickProfilesOptionOpensProfilesFragment() {
        // Click on the Scan QR Code menu item
        onView(ViewMatchers.withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(ViewMatchers.withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.scan_qr_code));

        // Verify that QRCodeActivity is started
        intended(hasComponent(QRcodeActivity.class.getName()));
    }

    @Test
    public void clickScanQrCodeOpensQrCodeActivity() {
        // Click on the Filters menu item
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.menu_community));
        onView(withId(R.id.drawer_fragment_container)).check(matches(isDisplayed()));
        onView(withId(R.id.profiles_activity_id)).check(matches(isDisplayed()));
    }

    @Test
    public void clickLogoutOptionOpensLoginActivity() {

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        // Click on the Logout menu item
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.menu_logout));
        intended(hasComponent(GoogleLoginActivity.class.getName()));
    }


    @Test
    public void drawerIsClosedBeforeClick() {
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(GravityCompat.START)));
    }

    @Test
    public void drawerIsOpenedAfterClick() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.drawer_layout)).check(matches(isOpen(GravityCompat.START)));
    }
}