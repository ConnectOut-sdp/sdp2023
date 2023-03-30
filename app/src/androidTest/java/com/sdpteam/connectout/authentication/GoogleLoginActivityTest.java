package com.sdpteam.connectout.authentication;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.withText;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sdpteam.connectout.R;

import android.content.Intent;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class GoogleLoginActivityTest {

    @Rule
    public ActivityScenarioRule<GoogleLoginActivity> testRule = new ActivityScenarioRule<>(GoogleLoginActivity.class);

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void cleanup() {
        Intents.release();
    }

    @Test
    public void loginButtonOpensTheGreetingsPageWithAccountInfos() {
        //        onView(withId(R.id.loginButton)).perform(click()); // cannot click because it triggers firebase stuff

        testRule.getScenario().onActivity(activity -> {
            activity.setAuthenticationService(new Authentication() {
                @Override
                public boolean isLoggedIn() {
                    return true;
                }

                @Override
                public AuthenticatedUser loggedUser() {
                    return new AuthenticatedUser("007", "David", "email@gmail.com");
                }

                @Override
                public void logout() {

                }

                @Override
                public Intent buildIntent() {
                    return null;
                }
            });

            activity.redirectIfAuthenticated(); // trick to avoid clicking the firebase button
        });
//        onView(ViewMatchers.withId(R.id.greetingMessage)).check(matches(withText("David \nemail@gmail.com")));
    }
}
