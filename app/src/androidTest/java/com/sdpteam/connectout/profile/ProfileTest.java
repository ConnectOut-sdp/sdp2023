package com.sdpteam.connectout.profile;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.BundleMatchers.hasKey;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtraWithKey;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.sdpteam.connectout.profile.ProfileFragment.PASSED_ID_KEY;
import static com.sdpteam.connectout.profile.ProfileRateActivity.RATED_UID;
import static com.sdpteam.connectout.utils.FutureUtils.fJoin;
import static com.sdpteam.connectout.utils.FutureUtils.waitABit;
import static com.sdpteam.connectout.utils.RandomPath.generateRandomPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertNull;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.intent.matcher.IntentMatchers;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;

import com.sdpteam.connectout.QrCode.QRcodeModalActivity;
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

    private final String uid = generateRandomPath();

    @Rule
    public ActivityScenarioRule<ProfileActivity> testRule = new ActivityScenarioRule<>(ProfileActivity.class);

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void cleanup() {
        Intents.release();
        ProfileFirebaseDataSource model = new ProfileFirebaseDataSource();
        model.deleteProfile(uid);
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
        {
            Intent profileIntent = new Intent(ApplicationProvider.getApplicationContext(), ProfileActivity.class).putExtra(PASSED_ID_KEY, uid);
            activity.startActivity(profileIntent);
        });
        // test if buttonRateProfile is displayed
        onView(withId(R.id.buttonRatingEditProfile)).check(matches(isDisplayed()));
        // test intent
        onView(withId(R.id.buttonRatingEditProfile)).perform(click());
        intended(allOf(hasComponent(ProfileRateActivity.class.getName()),
                hasExtra(RATED_UID, uid)));
    }

    @Test
    public void loggedUserTest1() {
        loggedUserBaseCase("Toto", "toto@gmail.com", "This is my bio, Toto's life", Profile.Gender.MALE);
    }

    @Test
    public void shareQrCodeButton() {
        onView(withId(R.id.buttonSharePersonalQrCode)).perform(click());
        intended(hasComponent(QRcodeModalActivity.class.getName()));

        // Verify that the intent has the expected key
        intended(hasExtraWithKey("title"));
        intended(hasExtraWithKey("qrCodeData"));
    }

    public void loggedUserBaseCase(String name, String email, String bio, Profile.Gender gender) {
        Profile userProfile = new Profile(uid, name, email,
                bio, gender, 1, 1, "");

        ProfileFirebaseDataSource model = new ProfileFirebaseDataSource();
        fJoin(model.saveProfile(userProfile));

        Profile fetchedProfile = fJoin(model.fetchProfile(uid));
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

    @Test
    public void deleteProfileTest() {
        Profile userProfile = new Profile(uid, "test", "testmail@gmail.com",
                "test", Profile.Gender.MALE, 1, 1, "");
        ProfileFirebaseDataSource model = new ProfileFirebaseDataSource();
        fJoin(model.saveProfile(userProfile));
        model.deleteProfile(uid);
        waitABit();
        assertNull(fJoin(model.fetchProfile(uid)));
    }
}
