package com.sdpteam.connectout.profile;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.sdpteam.connectout.profile.ProfileActivity.PROFILE_UID;
import static com.sdpteam.connectout.profile.ProfileRateActivity.RATED_UID;
import static com.sdpteam.connectout.profile.ProfileRateTest.uid;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.core.Is.is;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.sdpteam.connectout.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;

public class ProfileTest {

    /**
     * Since these tests are fetching from the database, they are also testing the
     * Model
     */

    @Rule
    public ActivityScenarioRule<ProfileActivity> testRule = new ActivityScenarioRule<>(ProfileActivity.class);

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void cleanup() {
        Intents.release();
    }

    @Test
    public void testProfileDisplayed() {
        onView(withId(R.id.profileName)).check(matches(isDisplayed()));
        onView(withId(R.id.profileEmail)).check(matches(isDisplayed()));
        onView(withId(R.id.profileBio)).check(matches(isDisplayed()));
        onView(withId(R.id.profileGender)).check(matches(isDisplayed()));
    }

    @Test
    public void testEditProfileButton() {
        onView(withId(R.id.buttonRatingEditProfile)).perform(click());
        intended(hasComponent(EditProfileActivity.class.getName()));
    }

    @Test
    public void testRateButton() {
        testRule.getScenario().onActivity(activity ->
        {Intent profileIntent = new Intent(ApplicationProvider.getApplicationContext(), ProfileActivity.class).putExtra(PROFILE_UID, uid);
        activity.startActivity(profileIntent);
        });
        // test if buttonRateProfile is displayed
        onView(withId(R.id.buttonRatingEditProfile)).check(matches(isDisplayed()));
        // test intent
        onView(withId(R.id.buttonRatingEditProfile)).perform(click());
        intended(allOf(hasComponent(ProfileRateActivity.class.getName()),
                IntentMatchers.hasExtra(RATED_UID, uid)));
    }

    @Test
    public void loggedUserTest1() {
        loggedUserBaseCase("Toto", "toto@gmail.com", "This is my bio, Toto's life", Profile.Gender.MALE);
    }

    public void loggedUserBaseCase(String name, String email, String bio, Profile.Gender gender) {
        Profile userProfile = new Profile(EditProfileActivity.NULL_USER, name, email,
                bio, gender, 1, 1, "");

        ProfileFirebaseDataSource model = new ProfileFirebaseDataSource();
        model.saveProfile(userProfile).join();

        Profile fetchedProfile = model.fetchProfile(EditProfileActivity.NULL_USER).join();
        ViewMatchers.assertThat(fetchedProfile.getEmail(), is(email));
        ViewMatchers.assertThat(fetchedProfile.getName(), is(name));
        ViewMatchers.assertThat(fetchedProfile.getBio(), is(bio));
        ViewMatchers.assertThat(fetchedProfile.getGender(), is(gender));
    }

    @Test
    public void gettersAndSettersTest() {
        Profile p = new Profile("12342", "Donald Trump", "donaldtrump@gmail.com", "I'm so cool", Profile.Gender.OTHER, 1, 1, "");

        assertThat(p.getBio(), is("I'm so cool"));
        assertThat(p.getName(), is("Donald Trump"));
        assertThat(p.getEmail(), is("donaldtrump@gmail.com"));
        assertThat(p.getGender(), is(Profile.Gender.OTHER));
        assertThat(p.getId(), is("12342"));

        p = new Profile("12342", "ExPresident", "expresident@gmail.com", "I'm not cool", Profile.Gender.MALE, 1, 1, "");

        assertThat(p.getBio(), is("I'm not cool"));
        assertThat(p.getName(), is("ExPresident"));
        assertThat(p.getEmail(), is("expresident@gmail.com"));
        assertThat(p.getGender(), is(Profile.Gender.MALE));
        assertThat(p.getId(), is("12342"));
    }
}
