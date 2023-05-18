package com.sdpteam.connectout.post.view;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.intent.Intents.intended;
import static androidx.test.espresso.intent.matcher.IntentMatchers.hasComponent;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;
import static com.sdpteam.connectout.utils.FutureUtils.fJoin;
import static com.sdpteam.connectout.utils.FutureUtils.waitABit;
import static com.sdpteam.connectout.utils.RandomPath.generateRandomPath;
import static com.sdpteam.connectout.utils.WithIndexMatcher.withIndex;

import java.util.ArrayList;

import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.chat.comment.CommentsActivity;
import com.sdpteam.connectout.post.model.Post;
import com.sdpteam.connectout.post.model.PostFirebaseDataSource;

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

    private final static String POST_ID = "A_" + generateRandomPath();
    private final static String EVENT_ID = "A_" + generateRandomPath();
    private final static String COMMENT_ID = "A_" + generateRandomPath();
    private static ArrayList<String> imagesUrls = new ArrayList<>();

    private final static Post TEST_POST_EVENT = new Post(POST_ID, NULL_USER, EVENT_ID, COMMENT_ID, imagesUrls, 100, Post.PostVisibility.PUBLIC, "title", "desc");

    private final static Post TEST_POST_EVENT1 = new Post(POST_ID + "1", NULL_USER, EVENT_ID, COMMENT_ID, imagesUrls, 100, Post.PostVisibility.PUBLIC, "title", "desc");
    private final static Post TEST_POST_EVENT2 = new Post(POST_ID + "2", NULL_USER, EVENT_ID, COMMENT_ID, imagesUrls, 100, Post.PostVisibility.PUBLIC, "title", "desc");

    @BeforeClass
    public static void setUpClass() {
        imagesUrls.clear();
        imagesUrls.add("https://firebasestorage.googleapis.com/v0/b/my-project-test-a847f.appspot.com/o/1683269814271_40078ef6-3886-4c15-b013-940b8d3f1614" +
                ".jpg?alt=media&token=c5abae50-d2af-4361-bb75-d975f8460a6d");
        imagesUrls.add("https://firebasestorage.googleapis.com/v0/b/my-project-test-a847f.appspot.com/o/1683269290710_5e3659ce-9aea-4f90-aea2-64eb941d6a6c" +
                ".jpg?alt=media&token=590a4df0-12c1-491a-9b89-f621c8f2bbe3");

        fJoin(new PostFirebaseDataSource().savePost(TEST_POST_EVENT));
        fJoin(new PostFirebaseDataSource().savePost(TEST_POST_EVENT1));
        fJoin(new PostFirebaseDataSource().savePost(TEST_POST_EVENT2));
    }

    @AfterClass
    public static void tearDownClass() {
        fJoin(new PostFirebaseDataSource().deletePost(TEST_POST_EVENT.getId()));
        fJoin(new PostFirebaseDataSource().deletePost(TEST_POST_EVENT1.getId()));
        fJoin(new PostFirebaseDataSource().deletePost(TEST_POST_EVENT2.getId()));
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
        intended(hasComponent(CommentsActivity.class.getName()));
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