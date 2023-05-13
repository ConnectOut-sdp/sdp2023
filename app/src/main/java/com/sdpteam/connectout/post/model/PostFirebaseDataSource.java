package com.sdpteam.connectout.post.model;

import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.google.firebase.database.FirebaseDatabase;
import com.sdpteam.connectout.utils.Result;

public class PostFirebaseDataSource implements PostDataSource {
    private static final String POSTS = "Posts";

    //TODO fix through firebase task
    @Override
    public CompletableFuture<Result<Void>> savePost(Post post) {
        String newId = FirebaseDatabase.getInstance().getReference().child(POSTS).push().getKey();
        Post newPost = new Post(newId, post.getProfileId(), post.getEventId(), post.getCommentsChatId(), post.getImagesUrls(), post.getNbrLikes(), post.getVisibility(), post.getTitle(), post.getDescription());
        FirebaseDatabase.getInstance().getReference().child(POSTS).child(newPost.getId()).setValue(newPost);
        return CompletableFuture.completedFuture(null);
    }

    //TODO fix through firebase task
    @Override
    public CompletableFuture<Result<Void>> deletePost(String postId) {
        FirebaseDatabase.getInstance().getReference().child(POSTS).child(postId).removeValue();
        return CompletableFuture.completedFuture(null);
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
