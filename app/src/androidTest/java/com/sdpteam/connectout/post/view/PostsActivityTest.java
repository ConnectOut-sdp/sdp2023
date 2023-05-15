package com.sdpteam.connectout.post.view;

import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasExtra;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.sdpteam.connectout.chat.comment.CommentsActivity.PASSED_COMMENTS_KEY;
import static com.sdpteam.connectout.utils.FutureUtils.waitABit;
import static com.sdpteam.connectout.utils.WithIndexMatcher.withIndex;

import org.junit.After;
import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.chat.comment.CommentsActivity;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class PostsActivityTest {

    @Rule
    public ActivityScenarioRule<PostsActivity> testRule = new ActivityScenarioRule<>(PostsActivity.class);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setup() {
        Intents.init();
    }

    @After
    public void cleanup() {
        Intents.release();
    }

    @Test
    public void testListFragmentDisplayed() {
        onView(withId(R.id.posts_list_view)).check(matches(isDisplayed()));
        waitABit();
        waitABit(); // for images to load
    }

    @Test
    public void visibilityDisplayedAndClickable() {
        onView(withId(R.id.posts_list_view)).check(matches(isDisplayed()));
        onView(withIndex(withId(R.id.post_visibility_icon), 0)).perform(click()); // click this button on the first post in the list
    }

    @Test
    public void commentsButtonDisplayedAndClickable() {
        onView(withId(R.id.posts_list_view)).check(matches(isDisplayed()));
        onView(withIndex(withId(R.id.post_comments_button), 0)).perform(click()); // click this button on the first post in the list
        intended(hasComponent(CommentsActivity.class.getName()));
    }

    @Test
    public void likeButtonDisplayedAndClickable() {
        onView(withId(R.id.posts_list_view)).check(matches(isDisplayed()));
        onView(withIndex(withId(R.id.post_like_button), 0)).perform(click()); // click this button on the first post in the list
    }
}