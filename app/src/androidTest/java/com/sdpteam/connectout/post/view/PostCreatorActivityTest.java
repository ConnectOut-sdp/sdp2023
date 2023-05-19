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

import org.junit.Before;
import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.post.model.PostFirebaseDataSource;

import android.content.Intent;
import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.rule.ActivityTestRule;

@RunWith(AndroidJUnit4.class)
public class PostCreatorActivityTest {

    @Rule
    public ActivityTestRule<PostCreatorActivity> testRule = new ActivityTestRule<PostCreatorActivity>(PostCreatorActivity.class) {
        @Override
        protected Intent getActivityIntent() {
            Intent intent = new Intent(InstrumentationRegistry.getInstrumentation().getTargetContext(), PostCreatorActivity.class);
            intent.putExtra("profileId", "yourProfileId");
            intent.putExtra("eventId", "yourEventId");
            intent.putExtra("eventName", "yourEventName");
            return intent;
        }
    };

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Before
    public void setUp() throws Exception {
        PostCreatorActivity.TEST = true;
    }

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
        final String postId = testRule.getActivity().viewModel.statusMsgLiveData().getValue().value();
        assertNotNull(postId);
        assertTrue(fJoin(new PostFirebaseDataSource().deletePost(postId)).isSuccess());
    }
}
