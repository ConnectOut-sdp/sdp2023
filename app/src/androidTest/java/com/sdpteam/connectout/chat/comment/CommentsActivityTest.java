package com.sdpteam.connectout.chat.comment;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.sdpteam.connectout.chat.comment.CommentsActivity.PASSED_COMMENTS_KEY;
import static com.sdpteam.connectout.profile.Profile.NULL_USER;
import static com.sdpteam.connectout.utils.FutureUtils.waitABit;
import static com.sdpteam.connectout.utils.RandomPath.generateRandomPath;
import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.chat.ChatFirebaseDataSource;
import com.sdpteam.connectout.chat.ChatMessage;
import com.sdpteam.connectout.utils.TestMenuItem;

import android.content.Intent;
import android.view.MenuItem;
import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class CommentsActivityTest {
    private final String nameOfTestComments = generateRandomPath();
    private final ChatFirebaseDataSource model = new ChatFirebaseDataSource();
    @Rule
    public ActivityScenarioRule<CommentsActivity> activityRule =
            new ActivityScenarioRule<>(new Intent(ApplicationProvider.getApplicationContext(), CommentsActivity.class)
                    .putExtra(PASSED_COMMENTS_KEY, nameOfTestComments));

    @Before
    public final void setUp() {
        Intents.init();
        model.emptyTestMode(nameOfTestComments);
        waitABit();
    }

    @After
    public final void tearDown() {
        Intents.release();
        model.emptyTestMode(nameOfTestComments);
        waitABit();
    }

    @Test
    public void testOnOptionsItemSelectedWithHomeItemIdFinishActivity() {
        activityRule.getScenario().onActivity(activity ->
        {
            MenuItem menuItem = new TestMenuItem(android.R.id.home);
            // Assert that the activity should not be finished
            assertTrue(activity.onOptionsItemSelected(menuItem));
        });
    }

    @Test
    public void testOnOptionsItemSelectedWithNonHomeItemIdDoNotFinishActivity() {
        activityRule.getScenario().onActivity(activity ->
        {
            MenuItem menuItem = new TestMenuItem(android.R.id.copy);
            // Assert that the activity should not be finished
            assertFalse(activity.onOptionsItemSelected(menuItem));
        });
    }

    @Test
    public void testListUsersDisplayed() {
        onView(ViewMatchers.withId(R.id.activity_comments)).check(matches(isDisplayed()));
        model.saveMessage(new ChatMessage("Bob", "Bob userId", "XD", nameOfTestComments));
        model.saveMessage(new ChatMessage("Alice", "Alice userId", "TOO FUNNY", nameOfTestComments));
        model.saveMessage(new ChatMessage("Maxence", "Maxence userId", "Pourquoi il ne pleut pas \uD83D\uDE22", nameOfTestComments));
        model.saveMessage(new ChatMessage("Charly", "Charly userId", "I wish I was here with you!", nameOfTestComments));
        model.saveMessage(new ChatMessage("Dylan", "Dylan userId", "FIRST", nameOfTestComments));
        waitABit();
        checkListViewValue(0, "XD", "Bob");
        checkListViewValue(1, "TOO FUNNY", "Alice");
        checkListViewValue(2, "Pourquoi il ne pleut pas \uD83D\uDE22", "Maxence");
        checkListViewValue(3, "I wish I was here with you!", "Charly");
        checkListViewValue(4, "FIRST", "Dylan");

        onView(withId(R.id.comment_input)).perform(typeText("Cool activity"));
        onView(withId(R.id.comment_post)).perform(click());
        waitABit();
        checkListViewValue(5, "Cool activity", NULL_USER);

        onView(withId(R.id.comment_input)).perform(typeText("Hi beautiful people"));
        onView(withId(R.id.comment_post)).perform(click());
        waitABit();
        checkListViewValue(6, "Hi beautiful people", NULL_USER);
    }

    public void checkListViewValue(int position, String text, String name) {
        onData(anything()).inAdapterView(withId(R.id.list_of_comments)).atPosition(position).
                onChildView(withId(R.id.message_text))
                .check(matches(withText(text)));
        onData(anything()).inAdapterView(withId(R.id.list_of_comments)).atPosition(position).
                onChildView(withId(R.id.message_user))
                .check(matches(withText(name)));
    }
}
