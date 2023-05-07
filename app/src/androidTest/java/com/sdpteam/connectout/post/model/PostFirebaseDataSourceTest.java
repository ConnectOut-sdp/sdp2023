package com.sdpteam.connectout.post.model;

import static com.sdpteam.connectout.utils.FutureUtils.fJoin;
import static com.sdpteam.connectout.utils.FutureUtils.waitABit;
import static com.sdpteam.connectout.utils.RandomPath.generateRandomPath;
import static org.junit.Assert.assertFalse;

import java.util.ArrayList;

import org.junit.After;
import org.junit.Test;

public class PostFirebaseDataSourceTest {

    private final String postId1 = "A_" + generateRandomPath();
    private final String postId2 = "A_" + generateRandomPath();
    private final String postId3 = "A_" + generateRandomPath();

    private final PostFirebaseDataSource model = new PostFirebaseDataSource();

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
    public void methodsNotImplementedThrowErrorMsg() {
        Post post = new Post(postId1, "", "", "", new ArrayList<>(), 0, Post.PostVisibility.PUBLIC);
        assertFalse(fJoin(model.savePost(post)).isSuccess());
        assertFalse(fJoin(model.deletePost("invalid")).isSuccess());
        assertFalse(fJoin(model.fetchPost("invalid")).isSuccess());
        assertFalse(fJoin(model.fetchAllPosts("invalid")).isSuccess());
        assertFalse(fJoin(model.fetchAllPostsOfEvent("userId", "eventId")).isSuccess());
        assertFalse(fJoin(model.fetchPostMadeByUser("userId", "authorId")).isSuccess());
    }
}
