package com.sdpteam.connectout.post.view;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;
import static com.sdpteam.connectout.utils.FutureUtils.waitABit;
import static com.sdpteam.connectout.utils.RandomPath.generateRandomPath;
import static com.sdpteam.connectout.utils.WithIndexMatcher.withIndex;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.event.EventFirebaseDataSource;
import com.sdpteam.connectout.post.model.Post;
import com.sdpteam.connectout.post.model.PostFirebaseDataSource;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.espresso.intent.Intents;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import java.util.ArrayList;

@RunWith(AndroidJUnit4.class)
public class PostsActivityTest {

    @Rule
    public ActivityScenarioRule<PostsActivity> testRule = new ActivityScenarioRule<>(PostsActivity.class);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    private final static String POST_ID = "A_" + generateRandomPath();
    private final static String EVENT_ID = "A_" + generateRandomPath();
    private final static String COMMENT_ID = "A_" + generateRandomPath();

    private final static Post TEST_POST_EVENT = new Post(POST_ID, NULL_USER, EVENT_ID, COMMENT_ID, new ArrayList<>(), 100, Post.PostVisibility.PUBLIC, "title", "desc");
    private final static Post TEST_POST_EVENT1 = new Post(POST_ID + "1", NULL_USER, EVENT_ID, COMMENT_ID, new ArrayList<>(), 100, Post.PostVisibility.PUBLIC, "title", "desc");
    private final static Post TEST_POST_EVENT2 = new Post(POST_ID + "2", NULL_USER, EVENT_ID, COMMENT_ID, new ArrayList<>(), 100, Post.PostVisibility.PUBLIC, "title", "desc");


    @BeforeClass
    public static void setUpClass() {
        new PostFirebaseDataSource().savePost(TEST_POST_EVENT);
        new PostFirebaseDataSource().savePost(TEST_POST_EVENT1);
        new PostFirebaseDataSource().savePost(TEST_POST_EVENT2);
        waitABit();
    }

    @AfterClass
    public static void tearDownClass() {
        new PostFirebaseDataSource().deletePost(TEST_POST_EVENT.getId());
        new PostFirebaseDataSource().deletePost(TEST_POST_EVENT1.getId());
        new PostFirebaseDataSource().deletePost(TEST_POST_EVENT2.getId());
    }

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
        waitABit();
        waitABit();
        onView(withIndex(withId(R.id.post_visibility_icon), 0)).perform(click()); // click this button on the first post in the list
    }

    @Test
    public void commentsButtonDisplayedAndClickable() {
        onView(withId(R.id.posts_list_view)).check(matches(isDisplayed()));
        waitABit();
        waitABit();
        onView(withIndex(withId(R.id.post_comments_button), 0)).perform(click()); // click this button on the first post in the list
    }

    @Test
    public void likeButtonDisplayedAndClickable() {
        onView(withId(R.id.posts_list_view)).check(matches(isDisplayed()));
        waitABit();
        waitABit();
        waitABit();
        onView(withIndex(withId(R.id.post_like_button), 0)).perform(click()); // click this button on the first post in the list
    }
}