package com.sdpteam.connectout.userList;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;

import android.view.View;
import android.widget.ListAdapter;

import androidx.test.espresso.matcher.ViewMatchers;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.firebase.ui.database.FirebaseListOptions;
import com.sdpteam.connectout.R;
import com.sdpteam.connectout.chat.ChatMessage;
import com.sdpteam.connectout.chat.ChatModel;
import com.sdpteam.connectout.chat.ChatViewModel;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import java.util.ArrayList;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

@RunWith(AndroidJUnit4.class)
public class UserListActivityTest {

    @Rule
    public ActivityScenarioRule<UserListActivity> activityRule = new ActivityScenarioRule<>(UserListActivity.class);

    @Test
    public void testListUsersDisplayed() {
        onView(ViewMatchers.withId(R.id.container_users_listview)).check(matches(isDisplayed()));
        onView(withId(R.id.container_users_listview)).check(matches(isDisplayed()));
    }
}
