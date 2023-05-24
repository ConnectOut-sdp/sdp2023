package com.sdpteam.connectout.authentication;

import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;

import org.hamcrest.Matchers;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sdpteam.connectout.registration.CompleteRegistrationActivity;

import android.content.Intent;
import androidx.test.espresso.intent.Intents;
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

    @BeforeClass
    public static void setupClass() {
        GoogleLoginActivity.authentication = new Authentication() {
            @Override
            public boolean isLoggedIn() {
                return false;
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
        };
    }

    @After
    public void cleanup() {
        Intents.release();
    }

    @Test
    public void loginButtonOpensTheGreetingsPageWithAccountInfos() {
        // Check that the intent was sent with the correct extra
        intended(Matchers.allOf(hasComponent(CompleteRegistrationActivity.class.getName()),
                hasExtra(Matchers.equalTo("loginInfo"), Matchers.
                        equalTo("David \nemail@gmail.com"))));
    }
}
