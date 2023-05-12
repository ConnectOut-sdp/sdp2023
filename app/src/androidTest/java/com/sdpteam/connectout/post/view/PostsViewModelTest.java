package com.sdpteam.connectout.post.view;

import static com.sdpteam.connectout.utils.RandomPath.generateRandomPath;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;

import androidx.arch.core.executor.testing.InstantTaskExecutorRule;
import androidx.test.espresso.core.internal.deps.guava.base.Objects;

import com.sdpteam.connectout.post.model.Post;
import com.sdpteam.connectout.post.model.PostDataSource;
import com.sdpteam.connectout.post.view.PostsViewModel;
import com.sdpteam.connectout.utils.LiveDataTestUtil;
import com.sdpteam.connectout.utils.Result;

import org.junit.Rule;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PostsViewModelTest {

    private final static String POST_ID = "A_" + generateRandomPath();
    private final static String EVENT_ID = "A_" + generateRandomPath();
    private final static String PROFILE_ID = "A_" + generateRandomPath();
    private final static String AUTHOR_ID = "A_" + generateRandomPath();
    private final static String COMMENT_ID = "A_" + generateRandomPath();
    private final static String ERROR_MESSAGE = "ErrorTest";

    private final static Post TEST_POST_EVENT = new Post(POST_ID, PROFILE_ID, EVENT_ID, COMMENT_ID, new ArrayList<>(), 100, Post.PostVisibility.SEMIPRIVATE, "title", "desc");
    private final static Post TEST_POST_USER = new Post(POST_ID, PROFILE_ID, null, COMMENT_ID, new ArrayList<>(), 100, Post.PostVisibility.SEMIPRIVATE, "title", "desc");
    private final static Post TEST_POST_AUTHOR = new Post(POST_ID, AUTHOR_ID, null, COMMENT_ID, new ArrayList<>(), 100, Post.PostVisibility.PUBLIC, "title", "desc");

    @Rule
    public InstantTaskExecutorRule instantTaskExecutorRule = new InstantTaskExecutorRule();

    @Test
    public void getPostsWithEventsFindsCorrectPosts() {
        PostsViewModel viewModel = new PostsViewModel(new FakePostDataSource(false));
        viewModel.getPosts(PROFILE_ID, EVENT_ID, null);
        assertTrue(LiveDataTestUtil.getOrAwaitValue(viewModel.getPostsLiveData()).contains(TEST_POST_EVENT));
        assertTrue(LiveDataTestUtil.getOrAwaitValue(viewModel.getPostsLiveData()).contains(TEST_POST_USER));
    }

    @Test
    public void getPostsWithAuthorFindsCorrectPosts() {
        PostsViewModel viewModel = new PostsViewModel(new FakePostDataSource(false));
        viewModel.getPosts(PROFILE_ID, null, AUTHOR_ID);
        assertTrue(LiveDataTestUtil.getOrAwaitValue(viewModel.getPostsLiveData()).contains(TEST_POST_AUTHOR));
    }

    @Test
    public void getAllPostsFindsCorrectPosts() {
        PostsViewModel viewModel = new PostsViewModel(new FakePostDataSource(false));
        viewModel.getPosts(PROFILE_ID, null, AUTHOR_ID);
        assertTrue(LiveDataTestUtil.getOrAwaitValue(viewModel.getPostsLiveData()).contains(TEST_POST_USER));
        assertTrue(LiveDataTestUtil.getOrAwaitValue(viewModel.getPostsLiveData()).contains(TEST_POST_EVENT));
        assertTrue(LiveDataTestUtil.getOrAwaitValue(viewModel.getPostsLiveData()).contains(TEST_POST_AUTHOR));
    }

    @Test
    public void getAllWithErrorSetsErrorMessage() {
        PostsViewModel viewModel = new PostsViewModel(new FakePostDataSource(true));
        viewModel.getPosts(PROFILE_ID, null, null);
        assertThat(LiveDataTestUtil.getOrAwaitValue(viewModel.getErrorMessage()), is(ERROR_MESSAGE));
    }

    @Test
    public void getPostsWithAuthorWithErrorSetsErrorMessage() {
        PostsViewModel viewModel = new PostsViewModel(new FakePostDataSource(true));
        viewModel.getPosts(PROFILE_ID, null, AUTHOR_ID);
        assertThat(LiveDataTestUtil.getOrAwaitValue(viewModel.getErrorMessage()), is(ERROR_MESSAGE));
    }

    @Test
    public void getPostsWithNoUserIdDoesNothing() {
        PostsViewModel viewModel = new PostsViewModel(new FakePostDataSource(true));
        List<Post> expected = LiveDataTestUtil.getOrAwaitValue(viewModel.getPostsLiveData());
        viewModel.getPosts(null, EVENT_ID, null);
        List<Post> obtained = LiveDataTestUtil.getOrAwaitValue(viewModel.getPostsLiveData());
        assertTrue(Objects.equal(obtained, expected));
    }

    @Test
    public void refreshDoesNothingWithNullUserId() {
        PostsViewModel viewModel = new PostsViewModel(new FakePostDataSource(false));
        viewModel.refreshPosts();
    }

    @Test
    public void refreshReturnsLastEventGiven() {
        PostsViewModel viewModel = new PostsViewModel(new FakePostDataSource(false));
        viewModel.getPosts(PROFILE_ID, EVENT_ID, null);
        List<Post> expected = LiveDataTestUtil.getOrAwaitValue(viewModel.getPostsLiveData());
        viewModel.refreshPosts();
        List<Post> obtained = LiveDataTestUtil.getOrAwaitValue(viewModel.getPostsLiveData());
        assertTrue(Objects.equal(obtained, expected));
    }

    @Test
    public void refreshReturnsAllGiven() {
        PostsViewModel viewModel = new PostsViewModel(new FakePostDataSource(false));
        viewModel.getPosts(PROFILE_ID, null, null);
        List<Post> expected = LiveDataTestUtil.getOrAwaitValue(viewModel.getPostsLiveData());
        viewModel.refreshPosts();
        List<Post> obtained = LiveDataTestUtil.getOrAwaitValue(viewModel.getPostsLiveData());
        assertTrue(Objects.equal(obtained, expected));
    }

    @Test
    public void refreshReturnsAuthorGiven() {
        PostsViewModel viewModel = new PostsViewModel(new FakePostDataSource(false));
        viewModel.getPosts(PROFILE_ID, null, AUTHOR_ID);
        List<Post> expected = LiveDataTestUtil.getOrAwaitValue(viewModel.getPostsLiveData());
        viewModel.refreshPosts();
        List<Post> obtained = LiveDataTestUtil.getOrAwaitValue(viewModel.getPostsLiveData());
        assertTrue(Objects.equal(obtained, expected));
    }

    private static class FakePostDataSource implements PostDataSource {
        private final boolean withError;

        public FakePostDataSource(boolean withError) {
            this.withError = withError;
        }

        @Override
        public CompletableFuture<Result<Void>> savePost(Post post) {
            return null;
        }

        @Override
        public CompletableFuture<Result<Void>> deletePost(String postId) {
            return null;
        }

        @Override
        public CompletableFuture<Result<Post>> fetchPost(String postId) {
            return null;
        }

        @Override
        public CompletableFuture<Result<List<Post>>> fetchAllPosts(String userId) {
            if (withError) {
                return CompletableFuture.completedFuture(new Result<>(null, false, ERROR_MESSAGE));
            }
            List<Post> lp = new ArrayList<>();
            lp.add(TEST_POST_USER);
            lp.add(TEST_POST_EVENT);
            lp.add(TEST_POST_AUTHOR);
            return CompletableFuture.completedFuture(new Result<>(lp, true, ""));
        }

        @Override
        public CompletableFuture<Result<List<Post>>> fetchAllPostsOfEvent(String userId, String eventId) {
            if (withError) {
                return CompletableFuture.completedFuture(new Result<>(null, false, ERROR_MESSAGE));
            }
            List<Post> lp = new ArrayList<>();
            lp.add(TEST_POST_EVENT);
            return CompletableFuture.completedFuture(new Result<>(lp, true, ""));
        }

        @Override
        public CompletableFuture<Result<List<Post>>> fetchPostMadeByUser(String userId, String authorId) {
            if (withError) {
                return CompletableFuture.completedFuture(new Result<>(null, false, ERROR_MESSAGE));
            }
            List<Post> lp = new ArrayList<>();
            lp.add(TEST_POST_AUTHOR);
            return CompletableFuture.completedFuture(new Result<>(lp, true, ""));
        }
    }
}
