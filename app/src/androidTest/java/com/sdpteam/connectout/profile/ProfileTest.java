package com.sdpteam.connectout.profile;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.Is.is;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.utils.LiveDataTestUtil;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ProfileTest {
    /**
     * Since these tests are fetching from the database, they are also testing the
     * Model
     */

    @Rule
    public ActivityScenarioRule<ProfileActivity> testRule = new ActivityScenarioRule<>(ProfileActivity.class);

    @Before
    public void setup() { Intents.init(); }

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
        onView(withId(R.id.buttonEditProfile)).perform(click());
        intended(hasComponent(EditProfileActivity.class.getName()));
    }

    @Test
    public void loggedUserTest1() {
        loggedUserBaseCase("Toto", "toto@gmail.com", "This is my bio, Toto's life", Profile.Gender.MALE);
    }

    public void loggedUserBaseCase(String name, String email, String bio, Profile.Gender gender) {

        Profile userProfile = new Profile(EditProfileActivity.NULL_USER, name, email,
                bio, gender, 1, 1);

        ProfileModel model = new ProfileModel();
        model.saveValue(userProfile, EditProfileActivity.NULL_USER);

        Profile fetchedProfile = LiveDataTestUtil.toCompletableFuture(model.getProfile(EditProfileActivity.NULL_USER)).join();
        assertThat(fetchedProfile.getEmail(), is(email));
        assertThat(fetchedProfile.getName(), is(name));
        assertThat(fetchedProfile.getBio(), is(bio));
        assertThat(fetchedProfile.getGender(), is(gender));

//        onView(withId(R.id.profileName)).check(matches(withText(name)));
//        onView(withId(R.id.profileEmail)).check(matches(withText(email)));
//        onView(withId(R.id.profileBio)).check(matches(withText(bio)));
//        onView(withId(R.id.profileGender)).check(matches(withText(gender.name())));
    }

    // case current user

    // show edit profile button

    // case other user

    // do not show edit profile button
}
