package com.sdpteam.connectout.profile;

import static android.app.PendingIntent.getActivity;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.app.Activity;
import android.content.Intent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.Is.is;

import com.google.gson.Gson;
import com.sdpteam.connectout.R;
import com.sdpteam.connectout.profile.EditProfileActivity;
import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.TOBEREMOVEDProfileActivity;
import com.sdpteam.connectout.utils.LiveDataTestUtil;

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

        Profile previousProfile = new Profile(EditProfileActivity.NULL_USER, "Luc", "bob@gmail.com",
                null, Profile.Gender.MALE, 1, 1);

        ProfileModel model = new ProfileModel();
        model.saveValue(previousProfile);

        onView(ViewMatchers.withId(R.id.editTextName)).perform(typeText("Aymeric"));
        onView(withId(R.id.editTextEmail)).perform(typeText("aymeric@gmail.com"));
        onView(withId(R.id.editTextBio)).perform(typeText("Love my friends, I'm on this app to make some more"));

        onView(withId(R.id.maleRadioButton)).perform(click());
        onView(withId(R.id.saveButton)).perform(click());

        Profile testProfile = new Profile(EditProfileActivity.NULL_USER, "Aymeric", "aymeric@gmail.com",
                "Love my friends, I'm on this app to make some more", Profile.Gender.MALE, 1, 1);

        Profile fetchedProfile = LiveDataTestUtil.toCompletableFuture(model.getValue()).join();

        assertThat(fetchedProfile.getEmail(), is(testProfile.getEmail()));
        assertThat(fetchedProfile.getName(), is(testProfile.getName()));
        assertThat(fetchedProfile.getId(), is(testProfile.getId()));
        assertThat(fetchedProfile.getBio(), is(testProfile.getBio()));
    }
}
