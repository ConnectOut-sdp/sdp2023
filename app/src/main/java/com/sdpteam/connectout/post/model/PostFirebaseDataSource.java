package com.sdpteam.connectout.post.model;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.sdpteam.connectout.utils.Result;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class PostFirebaseDataSource implements PostDataSource {
    private static final String POSTS = "Posts";

    //TODO fix through firebase task
    @Override
    public CompletableFuture<Result<Void>> savePost(Post post) {
        FirebaseDatabase.getInstance().getReference().child(POSTS).child(post.getId()).setValue(post);
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

    //TODO fix through firebase task
    @Override
    public CompletableFuture<Result<List<Post>>> fetchAllPosts(String userId) {
        CompletableFuture<Result<List<Post>>> future = new CompletableFuture<>();
        FirebaseDatabase.getInstance().getReference().child(POSTS)
                .orderByKey()
                .get()
                .addOnCompleteListener(t -> {

                    List<Post> posts = new ArrayList<>();
                    for (DataSnapshot child : t.getResult().getChildren()) {
                        Post post = child.getValue(Post.class);
                        posts.add(post);
                    }
                    future.complete(new Result<>(posts, true, ""));
                });

        return future;
    }
    //TODO fix through firebase task
    @Override
    public CompletableFuture<Result<List<Post>>> fetchAllPostsOfEvent(String userId, String eventId) {
        return fetchAllPosts(userId);
    }

    //TODO fix through firebase task
    @Override
    public CompletableFuture<Result<List<Post>>> fetchPostMadeByUser(String userId, String authorId) {
        return fetchAllPosts(userId);
    }
}
