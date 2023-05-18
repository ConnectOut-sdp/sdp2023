package com.sdpteam.connectout.post.model;

import static com.sdpteam.connectout.utils.FutureUtils.fJoin;
import static com.sdpteam.connectout.utils.FutureUtils.waitABit;
import static com.sdpteam.connectout.utils.RandomPath.generateRandomPath;
import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.Before;
import org.junit.Test;

import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.EventFirebaseDataSource;
import com.sdpteam.connectout.utils.Result;

//@RunWith(MockitoJUnitRunner.class)
public class PostFirebaseDataSourceTest {

    private final PostFirebaseDataSource model = new PostFirebaseDataSource();

    private final ArrayList<String> imagesUrls = new ArrayList<>();

    @Before
    public void setUp() throws Exception {
        imagesUrls.clear();
        imagesUrls.add("url1");
        imagesUrls.add("url2");
        imagesUrls.add("url3");

        // MockitoAnnotations.initMocks(this);
        // does not work, cannot mock the Task class so it does a isSuccessful() of false
    }

    @Test
    public void createPost() {
        // providing null postId will create a new one
        Post post = new Post(null, "sfsaf", "dgdsg", "dsgs", imagesUrls, 0, Post.PostVisibility.PUBLIC, "", "");
        Result<String> result = fJoin(model.savePost(post));
        assertTrue(result.isSuccess());
        String postId = result.value();

        assertTrue(fJoin(model.deletePost(postId)).isSuccess());
    }

    @Test
    public void savePostAndDeleteIt() {
        final String postId1 = "P0_" + generateRandomPath();
        Post post = new Post(postId1, "sfsaf", "dgdsg", "dsgs", imagesUrls, 0, Post.PostVisibility.PUBLIC, "", "");
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

        final String postId = "P1_" + generateRandomPath();
        Post post = new Post(postId, "pid", eventId, "commentsId", imagesUrls, 10, Post.PostVisibility.PUBLIC, "", "");
        assertTrue(fJoin(model.savePost(post)).isSuccess());

        Result<Post> fetchedResult = fJoin(model.fetchPost(postId));
        assertTrue(fetchedResult.isSuccess());
        Post fetchedPost = fetchedResult.value();
        assertEquals(fetchedPost, post);

        assertTrue(fJoin(model.deletePost(postId)).isSuccess());
        assertTrue(eventFirebaseDataSource.deleteEvent(event.getId()));
        waitABit();
    }

    @Test
    public void fetchPublicPost() {
        final String eventId = "eventId" + generateRandomPath();
        EventFirebaseDataSource eventFirebaseDataSource = new EventFirebaseDataSource();
        assertTrue(eventFirebaseDataSource.saveEvent(new Event(eventId, "", "", null, "")));
        waitABit();

        String postId = "P2_" + generateRandomPath();
        Post post = new Post(postId, "pid", eventId, "commentsId", imagesUrls, 10, Post.PostVisibility.PUBLIC, "", "");
        assertTrue(fJoin(model.savePost(post)).isSuccess());

        Result<Post> result = fJoin(model.fetchPost(postId));
        assertTrue(result.isSuccess());
        assertEquals(result.value().getId(), postId);
        assertEquals(Post.PostVisibility.PUBLIC, result.value().getVisibility());

        assertTrue(fJoin(model.deletePost(postId)).isSuccess());
        assertTrue(eventFirebaseDataSource.deleteEvent(eventId));
    }

    @Test
    public void fetchPostSemiPrivateChecked() {
        String postAuthorId = "pid";
        String organizerId = "pid";

        final String eventId = "eventId" + generateRandomPath();
        EventFirebaseDataSource eventFirebaseDataSource = new EventFirebaseDataSource();
        assertTrue(eventFirebaseDataSource.saveEvent(new Event(eventId, "", "", null, organizerId)));
        waitABit();
        waitABit();

        String postId = "P3_" + generateRandomPath();
        Post post = new Post(postId, postAuthorId, eventId, "commentsId", imagesUrls, 10, Post.PostVisibility.SEMIPRIVATE, "", "");
        assertTrue(fJoin(model.savePost(post)).isSuccess());

        Result<Post> result = (model.fetchPost(postId)).join();
        assertTrue(result.isSuccess());
        assertEquals(result.value(), post);

        assertTrue(fJoin(model.deletePost(postId)).isSuccess());
        assertTrue(eventFirebaseDataSource.deleteEvent(eventId));
    }

    @Test
    public void fetchPostSemiPrivateCheckedFails() {
        String postAuthorId = "pid";
        String organizerId = "pidDifferent";

        final String eventId = "eventId" + generateRandomPath();
        EventFirebaseDataSource eventFirebaseDataSource = new EventFirebaseDataSource();
        assertTrue(eventFirebaseDataSource.saveEvent(new Event(eventId, "", "", null, organizerId)));
        waitABit();
        waitABit();

        String postId = "P4_" + generateRandomPath();
        Post post = new Post(postId, postAuthorId, eventId, "commentsId", imagesUrls, 10, Post.PostVisibility.SEMIPRIVATE, "", "");
        assertTrue(fJoin(model.savePost(post)).isSuccess());

        Result<Post> result = (model.fetchPost(postId)).join();
        assertFalse(result.isSuccess());
        assertEquals(result.msg(), "Error occurred, user has not access to this post");

        assertTrue(fJoin(model.deletePost(postId)).isSuccess());
        assertTrue(eventFirebaseDataSource.deleteEvent(eventId));
    }

    @Test
    public void fetchPostWithNonExistingEventShouldFail() {
        String postId = "P5_" + generateRandomPath();
        String eventId = "eventIdNotExists";
        Post post = new Post(postId, "pid", eventId, "commentsId", imagesUrls, 10, Post.PostVisibility.SEMIPRIVATE, "", "");
        assertTrue(fJoin(model.savePost(post)).isSuccess());

        Result<Post> result = fJoin(model.fetchPost(postId));
        assertFalse(result.isSuccess());
        assertEquals(result.msg(), "Error the event associated to the post does not exist! (maybe a timeout issue) postId" + postId + " eventId" + eventId);

        assertTrue(fJoin(model.deletePost(postId)).isSuccess());
    }

    @Test
    public void failsSavePostResultIsNotSuccess() {
        final String postId = "P6_" + generateRandomPath();
        PostFirebaseDataSource.FORCE_FAIL = true;
        Post post = new Post(postId, "pid", "eventId", "commentsId", imagesUrls, 10, Post.PostVisibility.PUBLIC, "", "");
        assertFalse(fJoin(model.savePost(post)).isSuccess());
        PostFirebaseDataSource.FORCE_FAIL = false;

        assertTrue(fJoin(model.deletePost(postId)).isSuccess());
    }

    @Test
    public void postWithUnsupportedVisibility() {
        Post.PostVisibility visibility = null;
        final String postId = "P7_" + generateRandomPath();
        Post post = new Post(postId, "pid", "eventId", "commentsId", imagesUrls, 10, visibility, "", "");
        assertTrue(fJoin(model.savePost(post)).isSuccess());

        Result<Post> result = fJoin(model.fetchPost(postId));
        assertFalse(result.isSuccess());
        assertEquals(result.msg(), "Event has visibility set to NULL which is not expected");

        assertTrue(fJoin(model.deletePost(postId)).isSuccess());
    }

    @Test
    public void fetchAllPostsNetworkErrorResultIsNotSuccess() {
        PostFirebaseDataSource.FORCE_FAIL = true; // simulate network error
        Result<List<Post>> fetchedResult = fJoin(model.fetchAllPosts("id"));
        assertFalse(fetchedResult.isSuccess());
        PostFirebaseDataSource.FORCE_FAIL = false;
    }

    @Test
    public void deleteNonExistingPostResultIsNotSuccess() {
        PostFirebaseDataSource.FORCE_FAIL = true;
        Result<String> deleteResult = fJoin(model.deletePost("invalid"));
        assertFalse(deleteResult.isSuccess());
        PostFirebaseDataSource.FORCE_FAIL = false;
    }

    @Test
    public void failedFetchAllPostsOfEvent() {
        PostFirebaseDataSource.FORCE_FAIL = true;
        Result<List<Post>> fetchedResult = fJoin(model.fetchAllPostsOfEvent("id", "id2"));
        assertFalse(fetchedResult.isSuccess());
        PostFirebaseDataSource.FORCE_FAIL = false;
    }

    @Test
    public void fetchNonExistingPostResultIsNotSuccess() {
        Result<Post> fetchedResult = fJoin(model.fetchPost("invalid"));
        assertFalse(fetchedResult.isSuccess());
    }

    @Test
    public void fetchPostsMadeByUserWithCrash() {
        PostFirebaseDataSource.FORCE_FAIL = true;
        Result<List<Post>> listResult = fJoin(model.fetchPostMadeByUser("uid1", "auth1"));
        assertFalse(listResult.isSuccess());
        PostFirebaseDataSource.FORCE_FAIL = false;
    }

    @Test
    public void getAllPostsWorks() {
        String pid = "pid_" + generateRandomPath();
        final String postId1 = "P8a_" + generateRandomPath();
        final String postId2 = "P8b_" + generateRandomPath();
        final String postId3 = "P8c_" + generateRandomPath();

        Post post1 = new Post(postId1, pid, "eventId", "commentsId", imagesUrls, 10, Post.PostVisibility.PUBLIC, "", "");
        Post post2 = new Post(postId2, pid, "eventId", "commentsId", imagesUrls, 10, Post.PostVisibility.PUBLIC, "", "");
        Post post3 = new Post(postId3, pid, "eventId", "commentsId", imagesUrls, 10, Post.PostVisibility.PUBLIC, "", "");
        assertTrue(fJoin(model.savePost(post1)).isSuccess());
        assertTrue(fJoin(model.savePost(post2)).isSuccess());
        assertTrue(fJoin(model.savePost(post3)).isSuccess());

        Result<List<Post>> fetchedResult = fJoin(model.fetchAllPosts(pid));
        assertTrue(fetchedResult.isSuccess());
        List<Post> fetchedPosts =
                fetchedResult.value().stream().filter(post -> post.getId().equals(postId1) || post.getId().equals(postId2) || post.getId().equals(postId3)).collect(Collectors.toList());
        assertThat(fetchedPosts.size(), is(3));
        assertTrue(fetchedPosts.contains(post1));
        assertTrue(fetchedPosts.contains(post2));
        assertTrue(fetchedPosts.contains(post3));

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

        final String postId1 = "P9a_" + generateRandomPath();
        final String postId2 = "P9b_" + generateRandomPath();
        final String postId3 = "P9c_" + generateRandomPath();

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
        List<Post> fetchedPosts =
                fetchedResult.value().stream().filter(post -> post.getId().equals(postId1) || post.getId().equals(postId2) || post.getId().equals(postId3)).collect(Collectors.toList());
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
        final String eventId = "eventId" + generateRandomPath();
        EventFirebaseDataSource eventFirebaseDataSource = new EventFirebaseDataSource();
        assertTrue(eventFirebaseDataSource.saveEvent(new Event(eventId, "", "", null, "")));
        waitABit();

        ArrayList<String> imagesUrls = new ArrayList<>();
        imagesUrls.add("url1");
        imagesUrls.add("url2");
        imagesUrls.add("url3");

        final String postId1 = "P10a_" + generateRandomPath();
        final String postId2 = "P10b_" + generateRandomPath();
        final String postId3 = "P10c_" + generateRandomPath();
        String pid = "pid_" + generateRandomPath();
        String pidSomeoneElse = "pid_" + generateRandomPath();

        Post post1 = new Post(postId1, pid, "notThisEventId", "commentsId", imagesUrls, 10, Post.PostVisibility.PUBLIC, "", "");
        Post post2 = new Post(postId2, pidSomeoneElse, eventId, "commentsId", imagesUrls, 10, Post.PostVisibility.PUBLIC, "", "");
        Post post3 = new Post(postId3, pid, eventId, "commentsId", imagesUrls, 10, Post.PostVisibility.PUBLIC, "", "");
        assertTrue(fJoin(model.savePost(post1)).isSuccess());
        assertTrue(fJoin(model.savePost(post2)).isSuccess());
        assertTrue(fJoin(model.savePost(post3)).isSuccess());

        Result<List<Post>> fetchedResult = fJoin(model.fetchAllPostsOfEvent(pid, eventId));
        assertTrue(fetchedResult.isSuccess());
        List<Post> fetchedPosts =
                fetchedResult.value().stream().filter(post -> post.getId().equals(postId1) || post.getId().equals(postId2) || post.getId().equals(postId3)).collect(Collectors.toList());
        assertThat(fetchedPosts.size(), is(2));
        assertFalse(fetchedPosts.contains(post1));
        assertTrue(fetchedPosts.contains(post2));
        assertTrue(fetchedPosts.contains(post3));

        assertTrue(fJoin(model.deletePost(postId1)).isSuccess());
        assertTrue(fJoin(model.deletePost(postId2)).isSuccess());
        assertTrue(fJoin(model.deletePost(postId3)).isSuccess());

        assertTrue(eventFirebaseDataSource.deleteEvent(eventId));
        waitABit();
    }

    @Test
    public void fetchPostsMadeByNonExistingUserReturnsEmptyList() {
        Result<List<Post>> listResult = fJoin(model.fetchPostMadeByUser("uid1", "auth1"));
        assertTrue(listResult.isSuccess());
        assertTrue(listResult.value().isEmpty());
    }
}
