package com.sdpteam.connectout.chat;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static org.hamcrest.Matchers.anything;

import android.content.Intent;

import androidx.test.core.app.ApplicationProvider;
import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdpteam.connectout.R;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;


@RunWith(AndroidJUnit4.class)
public class ChatActivityTest {
    String nameOfTestChat = "TestChat";
    @Rule
    public ActivityScenarioRule<ChatActivity> activityRule = new ActivityScenarioRule<>(ChatActivity.class);

    @Before
    public final void setUp() {
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ChatActivity.class);
        intent.putExtra("chatId", nameOfTestChat);
        activityRule.getScenario().onActivity(activity -> {
            activity.startActivity(intent);
            activity.viewModel.chatFirebaseDataSource.emptyTestMode();
        });
    }

    @After
    public final void tearDown() {
        activityRule.getScenario().onActivity(activity ->
                activity.viewModel.chatFirebaseDataSource.emptyTestMode());
    }

    @Test
    public void testListUsersDisplayed() {
        onView(ViewMatchers.withId(R.id.activity_chat)).check(matches(isDisplayed()));
        activityRule.getScenario().onActivity(activity -> {
            ChatFirebaseDataSource model = activity.viewModel.chatFirebaseDataSource;
            model.saveMessage(new ChatMessage("Bob", "Bob userId", "Hey I m Bob", nameOfTestChat));
            model.saveMessage(new ChatMessage("Alice", "Alice userId", "Hey I m Alice", nameOfTestChat));
            model.saveMessage(new ChatMessage("Dylan", "Dylan userId", "Hey I m Dylan", nameOfTestChat));
            model.saveMessage(new ChatMessage("Alice", "Alice userId", "I think we should go swimming one of these days", nameOfTestChat));
            model.saveMessage(new ChatMessage("Dylan", "Dylan userId", "Excellent Idea", nameOfTestChat));
        });
        checkListViewValue(0, "Hey I m Bob", "Bob");
        checkListViewValue(1, "Hey I m Alice", "Alice");
        checkListViewValue(2, "Hey I m Dylan", "Dylan");
        checkListViewValue(3, "I think we should go swimming one of these days", "Alice");
        checkListViewValue(4, "Excellent Idea", "Dylan");

        onView(withId(R.id.chat_input)).perform(typeText("Excellent Idea"));
        onView(withId(R.id.chat_fab)).perform(click());
        checkListViewValue(5, "Excellent Idea", "You");

        onView(withId(R.id.chat_input)).perform(typeText("Hi beautiful people"));
        onView(withId(R.id.chat_fab)).perform(click());
        checkListViewValue(6, "Hi beautiful people", "You");
    }

    public void checkListViewValue(int position, String text, String name) {
        onData(anything()).inAdapterView(withId(R.id.list_of_messages)).atPosition(position).
                onChildView(withId(R.id.message_text)).
                check(matches(withText(text)));
        onData(anything()).inAdapterView(withId(R.id.list_of_messages)).atPosition(position).
                onChildView(withId(R.id.message_user)).
                check(matches(withText(name)));
    }
}
