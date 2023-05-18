package com.sdpteam.connectout.post.view;

import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.assertion.ViewAssertions.matches;
import static androidx.test.espresso.matcher.ViewMatchers.isDisplayed;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.espresso.matcher.ViewMatchers.withText;
import static com.sdpteam.connectout.utils.FutureUtils.fJoin;
import static com.sdpteam.connectout.utils.FutureUtils.waitABit;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.post.model.PostFirebaseDataSource;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.ext.junit.rules.ActivityScenarioRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class PostCreatorActivityTest {
    @Rule
    public ActivityScenarioRule<PostCreatorActivity> testRule = new ActivityScenarioRule<>(PostCreatorActivity.class);

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Test
    public void failedCreatePostShowsErrorMsg() {
        PostFirebaseDataSource.FORCE_FAIL = true;
        onView(withId(R.id.post_creator_save_button)).check(matches(isDisplayed()));
        onView(withId(R.id.post_creator_save_button)).perform(click());
        waitABit();
        onView(withId(R.id.post_creator_status)).check(matches(isDisplayed())).check(matches(withText("0 images uploaded successfully, but post creation failed!")));
        PostFirebaseDataSource.FORCE_FAIL = false;
    }

    @Test
    public void postCreationSuccess() {
        onView(withId(R.id.post_creator_save_button)).check(matches(isDisplayed()));
        onView(withId(R.id.post_creator_save_button)).perform(click());
        waitABit();
        onView(withId(R.id.post_creator_status)).check(matches(isDisplayed())).check(matches(withText("Post created successfully")));

        // cleanup
        final String[] postId = {null};
        testRule.getScenario().onActivity(activity -> {
            postId[0] = activity.viewModel.statusMsgLiveData().getValue().value();
        });
        assertNotNull(postId[0]);
        assertTrue(fJoin(new PostFirebaseDataSource().deletePost(postId[0])).isSuccess());
    }
}
