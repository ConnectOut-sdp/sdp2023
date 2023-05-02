package com.sdpteam.connectout.remoteStorage;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.Intents.intending;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasAction;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasType;
import static androidx.test.espresso.matcher.ViewMatchers.assertThat;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.is;

import android.app.Activity;
import android.app.Instrumentation;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.res.Resources;
import android.net.Uri;
import android.os.SystemClock;
import android.view.View;
import android.widget.ImageView;

import androidx.test.core.app.ActivityScenario;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.utils.TestActivity;

import org.hamcrest.Description;
import org.hamcrest.Matcher;
import org.hamcrest.TypeSafeMatcher;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ImageSelectionFragmentTest {

    @Rule
    public IntentsTestRule<TestActivity> intentsTestRule = new IntentsTestRule<>(TestActivity.class);

    private Uri mockedUri = null;

    public static Matcher<View> hasDrawable() {
        return new TypeSafeMatcher<View>() {

            @Override
            protected boolean matchesSafely(View item) {
                if (!(item instanceof ImageView)) {
                    return false;
                }
                ImageView imageView = (ImageView) item;
                return imageView.getDrawable() != null;
            }

            @Override
            public void describeTo(Description description) {
                description.appendText("has drawable");
            }
        };
    }

    @Test
    public void testImageSelection() {
        // Launch the TestActivity
        ActivityScenario<TestActivity> scenario = ActivityScenario.launch(TestActivity.class);

        // Add ImageSelectionFragment to the TestActivity
        scenario.onActivity(activity -> {
            ImageSelectionFragment fragment = new ImageSelectionFragment();
            fragment.setOnImageSelectedListener(imageUri -> {
                assertThat(imageUri.getPath(), is(mockedUri.getPath()));
            });
            activity.getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.activity_test_container, fragment)
                    .commitNow();
        });

        // Check if the ImageView and Button are displayed
        onView(withId(R.id.preview_image_view)).check(matches(isDisplayed()));
        onView(withId(R.id.choose_image_button)).check(matches(isDisplayed()));

        // Mock the Uri result for the image picker intent using the account_image drawable resource
        Resources resources = intentsTestRule.getActivity().getResources();
        mockedUri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(R.drawable.event_image) + '/' +
                resources.getResourceTypeName(R.drawable.event_image) + '/' +
                resources.getResourceEntryName(R.drawable.event_image));

        Intent resultData = new Intent();
        resultData.setData(mockedUri);
        Instrumentation.ActivityResult result = new Instrumentation.ActivityResult(Activity.RESULT_OK, resultData);
        intending(hasAction(Intent.ACTION_GET_CONTENT)).respondWith(result);
        SystemClock.sleep(2000);
        // Perform click on the "Choose Image" button
        onView(withId(R.id.choose_image_button)).perform(click());

        // Verify the intent with action GET_CONTENT and type "image/*" is launched
        intended(allOf(hasAction(Intent.ACTION_GET_CONTENT), hasType("image/*")));

        // Add a delay to allow the ImageView to load the mocked Uri
        SystemClock.sleep(2000);

        // Check if the ImageView has the drawable set from the mocked Uri
        onView(withId(R.id.preview_image_view)).check(matches(hasDrawable()));
    }
}
