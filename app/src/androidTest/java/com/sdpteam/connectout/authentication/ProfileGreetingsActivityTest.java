package com.sdpteam.connectout.authentication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.sdpteam.connectout.utils.FutureUtil.fJoin;
import static org.junit.Assert.assertEquals;

import java.util.concurrent.CompletableFuture;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sdpteam.connectout.R;

import android.content.Intent;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class ProfileGreetingsActivityTest {

    @Rule
    public ActivityScenarioRule<ProfileGreetingActivity> activityScenarioRule =
            new ActivityScenarioRule<>(ProfileGreetingActivity.class);


    public void testGreetingActivity() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ProfileGreetingActivity.class);
        intent.putExtra("loginInfo", "coucou lol");
        activityScenarioRule.getScenario().onActivity(activity -> {
            activity.startActivity(intent);
        });

        onView(ViewMatchers.withId(R.id.greetingMessage)).check(matches(isDisplayed()));
        onView(withId(R.id.greetingMessage)).check(matches(ViewMatchers.withText("coucou lol")));
    }

    @Test
    public void clickingLogoutPerformsTheLogout() {
        CompletableFuture<Boolean> logoutTriggered = new CompletableFuture<>();
        activityScenarioRule.getScenario().onActivity(activity -> {
            // Set the fake service provider on the activity
            activity.setAuthenticationService(new Authentication() {
                @Override
                public boolean isLoggedIn() {
                    return false;
                }

                @Override
                public AuthenticatedUser loggedUser() {
                    return null;
                }

                @Override
                public void logout() {
                    logoutTriggered.complete(true);
                }

                @Override
                public Intent buildIntent() {
                    return null;
                }
            });
        });
        onView(withId(R.id.logoutButton)).perform(click());
        assertEquals(true, fJoin(logoutTriggered));
    }
}
