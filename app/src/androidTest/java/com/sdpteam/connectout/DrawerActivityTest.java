package com.sdpteam.connectout;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.contrib.DrawerMatchers.isClosed;
import static androidx.test.espresso.contrib.DrawerMatchers.isOpen;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withContentDescription;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import static org.hamcrest.core.IsNot.not;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.widget.Toolbar;

import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.test.espresso.contrib.DrawerActions;
import androidx.test.espresso.contrib.NavigationViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.google.android.material.navigation.NavigationView;

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
    public void clickDrawerOptionHome_ShowsHomeFragment() {
        // Open the drawer
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        // Click on the Home menu item
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.menu_home));
        onView(withId(R.id.drawer_fragment_container)).check(matches(isDisplayed()));
    }

    @Test
    public void clickDrawerOptionMyAccount_ShowsMyAccountFragment() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        // Click on the My Account menu item
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.menu_my_account));
        onView(withId(R.id.drawer_fragment_container)).check(matches(isDisplayed()));
    }

    @Test
    public void clickDrawerOptionMyEvents_ShowsMyEventsFragment() {
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        // Click on the My Events menu item
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.menu_my_events));
        onView(withId(R.id.drawer_fragment_container)).check(matches(isDisplayed()));
    }

    @Test
    public void clickDrawerOptionFilters_ShowsFilterFragment() {
        // Click on the Filters menu item
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.menu_filters));
        onView(withId(R.id.drawer_fragment_container)).check(matches(isDisplayed()));
    }

    @Test
    public void clickDrawerOptionLogout_ShowsLoginActivity() {

        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());

        // Click on the Logout menu item
        onView(withId(R.id.nav_view)).perform(NavigationViewActions.navigateTo(R.id.menu_logout));
        intended(hasComponent(LogInActivity.class.getName()));
    }
    
    @Test
    public void drawerIsClosedBeforeInteraction(){
        onView(withId(R.id.drawer_layout)).check(matches(isClosed(GravityCompat.START)));
    }
    @Test
    public void drawerIsOpenedAfterClick(){
        onView(withId(R.id.drawer_layout)).perform(DrawerActions.open());
        onView(withId(R.id.drawer_layout)).check(matches(isOpen(GravityCompat.START)));
    }


}