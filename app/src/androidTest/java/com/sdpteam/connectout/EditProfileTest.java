package com.sdpteam.connectout;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.content.Intent;
import android.view.View;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import static org.hamcrest.Matchers.allOf;

import com.google.gson.Gson;
import com.sdpteam.connectout.profile.EditProfileActivity;
import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.TOBEREMOVEDProfileActivity;

import org.hamcrest.Matcher;
import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class EditProfileTest {
    @Rule
    public ActivityScenarioRule<EditProfileActivity> testRule = new ActivityScenarioRule<>(EditProfileActivity.class);

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void cleanup() {
        Intents.release();
    }

    @Test
    public void changeValuesTest(){
        Profile previousProfile = new Profile(1, "Bob", "bob@gmail.com",
                null, Profile.Gender.MALE);
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), EditProfileActivity.class);
        intent.putExtra("myProfile", new Gson().toJson(previousProfile));
        testRule.getScenario().onActivity(activity -> {
            activity.startActivity(intent);
        });

        Matcher<View> viewMatcherName = withId(R.id.editTextName);
        onView(viewMatcherName).perform(typeText("Aymeric"));
        Matcher<View> viewMatcherEmail = withId(R.id.editTextEmail);
        onView(viewMatcherEmail).perform(typeText("aymeric@gmail.com"));
        Matcher<View> viewMatcherBio = withId(R.id.editTextBio);
        onView(viewMatcherBio).perform(typeText("Love my friends, I'm on this app to make some more"));
        Matcher<View> viewMatcherGender = withId(R.id.maleRadioButton);
        onView(viewMatcherGender).perform(click());

        Profile testProfile = new Profile(1, "Aymeric", "aymeric@gmail.com",
                "Love my friends, I'm on this app to make some more", Profile.Gender.MALE);

        intended(allOf(IntentMatchers.hasComponent(TOBEREMOVEDProfileActivity.class.getName()),
                IntentMatchers.hasExtra("myProfile", new Gson().toJson(testProfile))));
    }
}
