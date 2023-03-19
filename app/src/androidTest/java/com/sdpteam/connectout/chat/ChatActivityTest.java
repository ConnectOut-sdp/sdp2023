package com.sdpteam.connectout.chat;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.action.ViewActions.typeText;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
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

    @Rule
    public ActivityScenarioRule<ChatActivity> activityRule = new ActivityScenarioRule<>(ChatActivity.class);

    @Before
    public final void setUp(){
        Intent intent = new Intent(ApplicationProvider.getApplicationContext(), ChatActivity.class);
        intent.putExtra("chatId", "chat Test");
        activityRule.getScenario().onActivity(activity -> {
            activity.startActivity(intent);
        });
        activityRule.getScenario().onActivity(activity ->
                activity.viewModel.chatModel.enterTestMode());
    }

    @After
    public final void tearDown(){
        activityRule.getScenario().onActivity(activity ->
                activity.viewModel.chatModel.emptyTestMode());
    }

    @Test
    public void testListUsersDisplayed() {
        onView(ViewMatchers.withId(R.id.activity_chat)).check(matches(isDisplayed()));


        //create chatMessages
        activityRule.getScenario().onActivity(activity ->{
            ChatModel model = activity.viewModel.chatModel;
            model.saveMessage(new ChatMessage("Bob", "Bob userId", "Hey I m Bob", "chat Test"));
            model.saveMessage(new ChatMessage("Alice", "Alice userId", "Hey I m Alice", "chat Test"));
            model.saveMessage(new ChatMessage("Dylan", "Dylan userId", "Hey I m Dylan", "chat Test"));
            model.saveMessage(new ChatMessage("Alice", "Alice userId", "I think we should go swimming one of these days", "chat Test"));
            model.saveMessage(new ChatMessage("Dylan", "Dylan userId", "Excellent Idea", "chat Test"));
        });

        onView(withId(R.id.chat_input)).perform(typeText("Hi beautiful people"));
        onView(withId(R.id.chat_fab)).perform(click());
        //onView(withId(R.id.activity_chat)).check()

        //onData(allOf(is(instanceOf(String.class)), startsWith("Hey I m Bob"))).check(matches(withText("Hey I m Bob")));
        /*onData(hasEntry(equalTo(ListViewSample.ROW_TEXT),is("Hey I m Bob")))

                .check(matches(isCompletelyDisplayed()));
         */
    }
}
