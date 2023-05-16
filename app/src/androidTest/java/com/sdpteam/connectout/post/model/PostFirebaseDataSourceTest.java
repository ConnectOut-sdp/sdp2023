package com.sdpteam.connectout.post.model;

import static com.sdpteam.connectout.utils.FutureUtils.fJoin;
import static com.sdpteam.connectout.utils.FutureUtils.waitABit;
import static com.sdpteam.connectout.utils.RandomPath.generateRandomPath;
import static org.junit.Assert.assertFalse;

import org.junit.After;
import org.junit.Test;

import java.util.ArrayList;

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

    //TODO fix this test
    @Test
    public void methodsNotImplementedThrowErrorMsg() {
        Post post = new Post(postId1, "", "", "", new ArrayList<>(), 0, Post.PostVisibility.PUBLIC, "title", "description");
        fJoin(model.savePost(post));
        fJoin(model.deletePost("invalid"));
        assertFalse(fJoin(model.fetchPost("invalid")).isSuccess());
        fJoin(model.fetchAllPosts("invalid"));
        fJoin(model.fetchAllPostsOfEvent("userId", "eventId"));
        fJoin(model.fetchPostMadeByUser("userId", "authorId"));
    }
}
