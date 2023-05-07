package com.sdpteam.connectout.post.model;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.sdpteam.connectout.utils.Result;

public class PostFirebaseDataSource implements PostDataSource {
    @Override
    public CompletableFuture<Result<Void>> savePost(Post post) {
        return CompletableFuture.completedFuture(new Result<>(null, false, "Not implemented"));
    }

    @Override
    public CompletableFuture<Result<Void>> deletePost(String postId) {
        return CompletableFuture.completedFuture(new Result<>(null, false, "Not implemented"));
    }

    @Override
    public CompletableFuture<Result<Post>> fetchPost(String postId) {
        return CompletableFuture.completedFuture(new Result<>(null, false, "Not implemented"));
    }

    @Override
    public CompletableFuture<Result<List<Post>>> fetchAllPosts(String userId) {
        return CompletableFuture.completedFuture(new Result<>(null, false, "Not implemented"));
    }

    @Override
    public CompletableFuture<Result<List<Post>>> fetchAllPostsOfEvent(String userId, String eventId) {
        return CompletableFuture.completedFuture(new Result<>(null, false, "Not implemented"));
    }

    @Override
    public CompletableFuture<Result<List<Post>>> fetchPostMadeByUser(String userId, String authorId) {
        return CompletableFuture.completedFuture(new Result<>(null, false, "Not implemented"));
    }
}
