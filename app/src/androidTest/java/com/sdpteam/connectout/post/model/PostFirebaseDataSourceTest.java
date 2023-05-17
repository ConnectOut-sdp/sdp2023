package com.sdpteam.connectout.post.model;

import static com.sdpteam.connectout.utils.FutureUtils.fJoin;
import static com.sdpteam.connectout.utils.FutureUtils.waitABit;
import static com.sdpteam.connectout.utils.RandomPath.generateRandomPath;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertSame;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.junit.Assert.fail;

import java.util.ArrayList;
import java.util.List;

import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.EventFirebaseDataSource;
import com.sdpteam.connectout.utils.Result;

//@RunWith(MockitoJUnitRunner.class)
public class PostFirebaseDataSourceTest {

    private final String postId1 = "A_" + generateRandomPath();
    private final String postId2 = "A_" + generateRandomPath();
    private final String postId3 = "A_" + generateRandomPath();

    private final PostFirebaseDataSource model = new PostFirebaseDataSource();

    private final ArrayList<String> imagesUrls = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        imagesUrls.clear();
        imagesUrls.add("url1");
        imagesUrls.add("url2");
        imagesUrls.add("url3");

//        MockitoAnnotations.initMocks(this);
// does not work, cannot mock the Task class so it does a isSuccessful() of false
    }

    @After
    public void cleanup() {
        fJoin(model.deletePost(postId1));
        fJoin(model.deletePost(postId2));
        fJoin(model.deletePost(postId3));
        waitABit();
    }

    //    @Test
//    public void modelReturnNullOnNonExistingEventEId() {
//        Result<Post> foundPost = fJoin(model.fetchPost("invalid"));
//        assertFalse(foundPost.isSuccess());
//    }

    @Test
    public void savePostAndDeleteIt() {
        Post post = new Post(postId1, "", "", "", imagesUrls, 0, Post.PostVisibility.PUBLIC, "", "");
        assertTrue(fJoin(model.savePost(post)).isSuccess());
        assertTrue(fJoin(model.deletePost(postId1)).isSuccess());
    }

    @Test
    public void saveAndFetchPostInfoIsConserved() {
        String eventId = generateRandomPath();
        Event event = new Event(eventId, "Title", "Desc", null, "orgId");
        EventFirebaseDataSource eventFirebaseDataSource = new EventFirebaseDataSource();
        assertTrue(eventFirebaseDataSource.saveEvent(event));
        waitABit();

        Post post = new Post(postId2, "pid", eventId, "commentsId", imagesUrls, 10, Post.PostVisibility.PUBLIC, "", "");
        assertTrue(fJoin(model.savePost(post)).isSuccess());

        Result<Post> fetchedResult = fJoin(model.fetchPost(postId2));
        assertTrue(fetchedResult.isSuccess());
        Post fetchedPost = fetchedResult.value();
        assertSamePosts(fetchedPost, post);

        assertTrue(eventFirebaseDataSource.deleteEvent(event.getId()));
        waitABit();
    }

    @Test
    public void failsSavePostResultIsNotSuccess() {
        PostFirebaseDataSource.forceFail = true;
        Post post = new Post(postId1, "pid", "eventId", "commentsId", imagesUrls, 10, Post.PostVisibility.PUBLIC, "", "");
        assertFalse(fJoin(model.savePost(post)).isSuccess());
        PostFirebaseDataSource.forceFail = false;
    }

    @Test
    public void deleteNonExistingPostResultIsNotSuccess() {
        PostFirebaseDataSource.forceFail = true;
        Result<String> deleteResult = fJoin(model.deletePost("invalid"));
        assertFalse(deleteResult.isSuccess());
        PostFirebaseDataSource.forceFail = false;
    }

    @Test
    public void fetchNonExistingPostResultIsNotSuccess() {
        Result<Post> fetchedResult = fJoin(model.fetchPost("invalid"));
        assertFalse(fetchedResult.isSuccess());
    }

    @Test
    public void getAllPostsWorks() {
        String pid = "pid_" + generateRandomPath();
        Post post1 = new Post(postId1, pid, "eventId", "commentsId", imagesUrls, 10, Post.PostVisibility.PUBLIC, "", "");
        Post post2 = new Post(postId2, pid, "eventId", "commentsId", imagesUrls, 10, Post.PostVisibility.PUBLIC, "", "");
        Post post3 = new Post(postId3, pid, "eventId", "commentsId", imagesUrls, 10, Post.PostVisibility.PUBLIC, "", "");
        assertTrue(fJoin(model.savePost(post1)).isSuccess());
        assertTrue(fJoin(model.savePost(post2)).isSuccess());
        assertTrue(fJoin(model.savePost(post3)).isSuccess());

        Result<List<Post>> fetchedResult = fJoin(model.fetchAllPosts(pid));
        assertTrue(fetchedResult.isSuccess());
        List<Post> fetchedPosts = fetchedResult.value();
        assertThat(fetchedPosts.size(), is(3));
        assertPostInList(post1, fetchedPosts);
        assertPostInList(post2, fetchedPosts);
        assertPostInList(post3, fetchedPosts);

        assertTrue(fJoin(model.deletePost(postId1)).isSuccess());
        assertTrue(fJoin(model.deletePost(postId2)).isSuccess());
        assertTrue(fJoin(model.deletePost(postId3)).isSuccess());
    }

    @Test
    public void getAllPostsFiltersOutNotPublicPosts() {
        ArrayList<String> imagesUrls = new ArrayList<>();
        imagesUrls.add("url1");
        imagesUrls.add("url2");
        imagesUrls.add("url3");
        String pid = "pid_" + generateRandomPath();
        String pidSomeoneElse = "pid_" + generateRandomPath();
        Post post1 = new Post(postId1, pid, "eventId", "commentsId", imagesUrls, 10, Post.PostVisibility.PUBLIC, "", "");
        Post post2 = new Post(postId2, pidSomeoneElse, "eventId", "commentsId", imagesUrls, 10, Post.PostVisibility.SEMIPRIVATE, "", "");
        Post post3 = new Post(postId3, pid, "eventId", "commentsId", imagesUrls, 10, Post.PostVisibility.PUBLIC, "", "");
        assertTrue(fJoin(model.savePost(post1)).isSuccess());
        assertTrue(fJoin(model.savePost(post2)).isSuccess());
        assertTrue(fJoin(model.savePost(post3)).isSuccess());

        Result<List<Post>> fetchedResult = fJoin(model.fetchAllPosts(pid));
        assertTrue(fetchedResult.isSuccess());
        List<Post> fetchedPosts = fetchedResult.value();
        assertThat(fetchedPosts.size(), is(2));
        assertTrue(fetchedPosts.contains(post1));
        assertFalse(fetchedPosts.contains(post2));
        assertTrue(fetchedPosts.contains(post3));

        assertTrue(fJoin(model.deletePost(postId1)).isSuccess());
        assertTrue(fJoin(model.deletePost(postId2)).isSuccess());
        assertTrue(fJoin(model.deletePost(postId3)).isSuccess());
    }

    @Test
    public void getAllPostsOfEvent() {
        ArrayList<String> imagesUrls = new ArrayList<>();
        imagesUrls.add("url1");
        imagesUrls.add("url2");
        imagesUrls.add("url3");
        String pid = "pid_" + generateRandomPath();
        String pidSomeoneElse = "pid_" + generateRandomPath();
        Post post1 = new Post(postId1, pid, "notThisEventId", "commentsId", imagesUrls, 10, Post.PostVisibility.PUBLIC, "", "");
        Post post2 = new Post(postId2, pidSomeoneElse, "eventId", "commentsId", imagesUrls, 10, Post.PostVisibility.PUBLIC, "", "");
        Post post3 = new Post(postId3, pid, "eventId", "commentsId", imagesUrls, 10, Post.PostVisibility.PUBLIC, "", "");
        assertTrue(fJoin(model.savePost(post1)).isSuccess());
        assertTrue(fJoin(model.savePost(post2)).isSuccess());
        assertTrue(fJoin(model.savePost(post3)).isSuccess());

        Result<List<Post>> fetchedResult = fJoin(model.fetchAllPostsOfEvent(pid, "eventId"));
        assertTrue(fetchedResult.isSuccess());
        List<Post> fetchedPosts = fetchedResult.value();
        assertThat(fetchedPosts.size(), is(2));
        assertPostInList(post1, fetchedPosts);
        assertPostInList(post2, fetchedPosts);
        assertPostInList(post3, fetchedPosts);

        assertTrue(fJoin(model.savePost(post1)).isSuccess());
        assertTrue(fJoin(model.savePost(post2)).isSuccess());
        assertTrue(fJoin(model.savePost(post3)).isSuccess());
    }

    private static void assertSamePosts(Post a, Post b) {
        assertEquals(a.getId(), b.getId());
        assertEquals(a.getProfileId(), b.getProfileId());
        assertEquals(a.getEventId(), b.getEventId());
        assertEquals(a.getCommentsChatId(), b.getCommentsChatId());
        assertEquals(a.getNbrLikes(), b.getNbrLikes());
        assertSame(a.getVisibility(), b.getVisibility());
        assertEquals(a.getImagesUrls().size(), b.getImagesUrls().size());
        assertTrue(b.getImagesUrls().containsAll(a.getImagesUrls()));
    }

    private static void assertPostInList(Post x, List<Post> list) {
        for (Post y : list) {
            if (x.getId().equals(y.getId())) {
                assertSamePosts(x, y);
                return;
            }
        }
        fail("Post " + x.getId() + " not found in list");
    }
}
