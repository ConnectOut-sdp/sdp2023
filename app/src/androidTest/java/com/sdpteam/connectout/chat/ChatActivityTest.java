package com.sdpteam.connectout.chat;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.sdpteam.connectout.utils.FutureUtils.waitABit;
import static com.sdpteam.connectout.utils.RandomPath.generateRandomPath;
import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import android.content.Intent;
import android.view.MenuItem;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.intent.Intents;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.utils.TestMenuItem;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

@RunWith(AndroidJUnit4.class)
public class ChatActivityTest {
    private final String nameOfTestChat = generateRandomPath();
    @Rule
    public ActivityScenarioRule<ChatActivity> activityRule = new ActivityScenarioRule<>(new Intent(ApplicationProvider.getApplicationContext(), ChatActivity.class).putExtra("chatId", nameOfTestChat));

    private final ChatFirebaseDataSource model = new ChatFirebaseDataSource();

    @Before
    public final void setUp() {
        Intents.init();
        model.emptyTestMode(nameOfTestChat);
        waitABit();
    }

    @After
    public final void tearDown() {
        Intents.release();
        model.emptyTestMode(nameOfTestChat);
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
        onView(ViewMatchers.withId(R.id.activity_chat)).check(matches(isDisplayed()));
        model.saveMessage(new ChatMessage("Bob", "Bob userId", "Hey I m Bob", nameOfTestChat));
        model.saveMessage(new ChatMessage("Alice", "Alice userId", "Hey I m Alice", nameOfTestChat));
        model.saveMessage(new ChatMessage("Dylan", "Dylan userId", "Hey I m Dylan", nameOfTestChat));
        model.saveMessage(new ChatMessage("Alice", "Alice userId", "I think we should go swimming one of these days", nameOfTestChat));
        model.saveMessage(new ChatMessage("Dylan", "Dylan userId", "Excellent Idea", nameOfTestChat));
        waitABit();
        checkListViewValue(0, "Hey I m Bob", "Bob");
        checkListViewValue(1, "Hey I m Alice", "Alice");
        checkListViewValue(2, "Hey I m Dylan", "Dylan");
        checkListViewValue(3, "I think we should go swimming one of these days", "Alice");
        checkListViewValue(4, "Excellent Idea", "Dylan");

        onView(withId(R.id.chat_input)).perform(typeText("Excellent Idea"));
        onView(withId(R.id.chat_fab)).perform(click());
        waitABit();
        checkListViewValue(5, "Excellent Idea", "You");

        onView(withId(R.id.chat_input)).perform(typeText("Hi beautiful people"));
        onView(withId(R.id.chat_fab)).perform(click());
        waitABit();
        checkListViewValue(6, "Hi beautiful people", "You");
    }

    public void checkListViewValue(int position, String text, String name) {
        onData(anything()).inAdapterView(withId(R.id.list_of_messages)).atPosition(position).
                onChildView(withId(R.id.message_text))
                .check(matches(withText(text)));
        onData(anything()).inAdapterView(withId(R.id.list_of_messages)).atPosition(position).
                onChildView(withId(R.id.message_user));
    }
}
