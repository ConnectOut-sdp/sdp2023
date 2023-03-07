package com.sdpteam.connectout.profile;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.core.Is.is;

import androidx.test.espresso.Espresso;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
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


public class EditProfileTest {
    /**
     * Since these tests are fetching from the database, they are also testing the
     * Model
     */
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
    public void changeValuesTestMale() {
        testDifferentValues("Aymeric", "aymeric@gmail.com",
                "Love my friends, I'm on this app to make some more", Profile.Gender.MALE);
    }

    @Test
    public void changeValuesTestFemale() {
        testDifferentValues("Bob", "bob@gmail.com",
                "empty", Profile.Gender.FEMALE);
    }

    @Test
    public void changeValuesTestOther() {
        testDifferentValues("Alice", "alice@gmail.com",
                "empty for now", Profile.Gender.OTHER);
    }

    @Test
    public void changeValuesTestNone() {
        testDifferentValues("why do you want to know?", "I have no email",
                "You want to know too much", null);
    }

    private static void testDifferentValues(String name, String email, String bio, Profile.Gender gender) {
        Profile previousProfile = new Profile(EditProfileActivity.NULL_USER, "bob", "bob@gmail.com",
                null, Profile.Gender.MALE, 1, 1);

        ProfileModel model = new ProfileModel();
        model.saveValue(previousProfile);

        onView(ViewMatchers.withId(R.id.editTextName)).perform(typeText(name));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.editTextEmail)).perform(typeText(email));
        Espresso.closeSoftKeyboard();
        onView(withId(R.id.editTextBio)).perform(typeText(bio));
        Espresso.closeSoftKeyboard();

        if (gender == Profile.Gender.MALE) {
            onView(withId(R.id.maleRadioButton)).perform(click());
        } else if ((gender == Profile.Gender.FEMALE)) {
            onView(withId(R.id.femaleRadioButton)).perform(click());
        } else if ((gender == Profile.Gender.OTHER)) {
            onView(withId(R.id.otherRadioButton)).perform(click());
        }
        Espresso.closeSoftKeyboard();

        onView(withId(R.id.saveButton)).perform(click());

        Profile fetchedProfile = LiveDataTestUtil.toCompletableFuture(model.getValue()).join();

        assertThat(fetchedProfile.getEmail(), is(email));
        assertThat(fetchedProfile.getName(), is(name));
        assertThat(fetchedProfile.getBio(), is(bio));
        assertThat(fetchedProfile.getGender(), is(gender));
    }
}
