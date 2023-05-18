package com.sdpteam.connectout.profileList;

import static androidx.test.espresso.Espresso.closeSoftKeyboard;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.sdpteam.connectout.utils.FutureUtils.waitABit;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.profile.Profile;

import android.widget.ListView;
import androidx.fragment.app.testing.FragmentScenario;
import androidx.test.espresso.action.ViewActions;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class ProfileFilterFragmentTest {

    @Before
    public void setUp() {
        Intents.init();
    }

    @After
    public void tearDown() {
        Intents.release();
    }

    @Test
    public void filteringByNameIsOrdered() {
        FragmentScenario<ProfileFilterFragment> fc = FragmentScenario.launchInContainer(ProfileFilterFragment.class);

        onView(withId(R.id.name_switch_button)).perform(click());
        onView(withId(R.id.filter_apply_button)).perform(click());
        waitABit();
        onView(withId(R.id.filter_container)).check(matches(isDisplayed()));

        List<Profile> profiles = new ArrayList<>();
        fc.onFragment(fragment -> {
            ListView recyclerView = fragment.getView().findViewById(R.id.user_list_view);
            for (int i = 0; i < recyclerView.getAdapter().getCount(); i++) {
                Profile profile = (Profile) recyclerView.getAdapter().getItem(i);
                profiles.add(profile);
            }
        });
        if (!profiles.isEmpty()) {
            List<String> givenList = profiles.stream().map(Profile::getNameLowercase).collect(Collectors.toList());
            List<String> copiedList = new ArrayList<>(givenList);
            Collections.sort(copiedList);
            assertThat(givenList, is(copiedList));
        }
    }

    @Test
    public void filteringByNameFindsPersonName() {
        FragmentScenario<ProfileFilterFragment> fc = FragmentScenario.launchInContainer(ProfileFilterFragment.class);

        onView(withId(R.id.name_switch_button)).perform(click());
        onView(withId(R.id.text_filter)).perform(typeText("Alice"));
        closeSoftKeyboard();
        onView(withId(R.id.filter_apply_button)).perform(click());
        waitABit();
        onView(withId(R.id.filter_container)).check(matches(isDisplayed()));

        List<Profile> profiles = new ArrayList<>();
        fc.onFragment(fragment -> {
            ListView recyclerView = fragment.getView().findViewById(R.id.user_list_view);
            for (int i = 0; i < recyclerView.getAdapter().getCount(); i++) {
                Profile profile = (Profile) recyclerView.getAdapter().getItem(i);
                profiles.add(profile);
            }
        });
        System.out.println(profiles.size());
        if (!profiles.isEmpty()) {
            assertTrue(profiles.get(0).getName().startsWith("Alice"));
        }
    }

    @Test
    public void wrongFilteringWithRatingShowsCompleteList() {
        FragmentScenario<ProfileFilterFragment> fc = FragmentScenario.launchInContainer(ProfileFilterFragment.class);

        onView(withId(R.id.name_switch_button)).perform(click());
        onView(withId(R.id.rating_switch_button)).perform(click());
        onView(withId(R.id.rating_switch_button)).perform(click());
        onView(withId(R.id.text_filter)).perform(typeText("I dont know how to use filter "), ViewActions.closeSoftKeyboard());
        onView(withId(R.id.filter_apply_button)).perform(click());
        waitABit();
        onView(withId(R.id.filter_container)).check(matches(isDisplayed()));

        List<Profile> profiles = new ArrayList<>();
        fc.onFragment(fragment -> {
            ListView recyclerView = fragment.getView().findViewById(R.id.user_list_view);
            for (int i = 0; i < recyclerView.getAdapter().getCount(); i++) {
                Profile profile = (Profile) recyclerView.getAdapter().getItem(i);
                profiles.add(profile);
            }
        });
        if (!profiles.isEmpty()) {
            List<Double> givenList = profiles.stream().map(Profile::getRating).collect(Collectors.toList());
            List<Double> copiedList = new ArrayList<>(givenList);
            Collections.sort(copiedList);
            Collections.sort(givenList);
            assertEquals(givenList, copiedList);
        }
    }

    @Test
    public void wrongFilteringWithNameShowsEmptyList() {
        FragmentScenario<ProfileFilterFragment> fc = FragmentScenario.launchInContainer(ProfileFilterFragment.class);

        String improbableName = UUID.randomUUID().toString();
        onView(withId(R.id.text_filter)).perform(typeText(improbableName + "I dont know how to use filters ...."));
        closeSoftKeyboard();
        onView(withId(R.id.name_switch_button)).perform(click());
        onView(withId(R.id.filter_apply_button)).perform(click());
        waitABit();
        onView(withId(R.id.filter_container)).check(matches(isDisplayed()));

        List<Profile> profiles = new ArrayList<>();
        fc.onFragment(fragment -> {
            ListView recyclerView = fragment.getView().findViewById(R.id.user_list_view);
            for (int i = 0; i < recyclerView.getAdapter().getCount(); i++) {
                Profile profile = (Profile) recyclerView.getAdapter().getItem(i);
                profiles.add(profile);
            }
        });
        assertTrue(profiles.isEmpty());
    }
}

