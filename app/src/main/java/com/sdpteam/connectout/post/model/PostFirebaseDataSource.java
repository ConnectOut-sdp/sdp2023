package com.sdpteam.connectout.post.model;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.sdpteam.connectout.utils.Result;

public class PostFirebaseDataSource implements PostDataSource {
    public static boolean forceFail = false; // workaround because Mocking the isSuccessful() of the Task class is not possible
    // (MockitoException: Mockito cannot mock this class: class com.google.android.gms.tasks.Task)

    private static final String POSTS = "Posts";

    @Override
    public CompletableFuture<Result<String>> savePost(Post post) {
        if (post.getId() == null) {
            String newId = FirebaseDatabase.getInstance().getReference().child(POSTS).push().getKey();
            post = new Post(newId, post.getProfileId(), post.getEventId(), post.getCommentsChatId(), post.getImagesUrls(), post.getNbrLikes(), post.getVisibility());
        }
        final Post finalPost = post;

        CompletableFuture<Result<String>> result = new CompletableFuture<>();

        FirebaseDatabase.getInstance().getReference().child(POSTS).child(post.getId()).setValue(post)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !forceFail) {
                        System.out.println("Post saved successfully");
                        result.complete(new Result<>(finalPost.getId(), true));
                    } else {
                        result.complete(new Result<>(null, false, "Post save failed"));
                    }
                });
        return result;
    }

    @Override
    public CompletableFuture<Result<String>> deletePost(String postId) {
        CompletableFuture<Result<String>> result = new CompletableFuture<>();
        FirebaseDatabase.getInstance().getReference().child(POSTS).child(postId).removeValue()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !forceFail) {
                        System.out.println("Post saved successfully");
                        result.complete(new Result<>(postId, true));
                    } else {
                        result.complete(new Result<>(null, false, "Post save failed"));
                    }
                });
        return result;
    }

    @Override
    public CompletableFuture<Result<Post>> fetchPost(String postId) {
        CompletableFuture<Result<Post>> result = new CompletableFuture<>();
        FirebaseDatabase.getInstance().getReference().child(POSTS).child(postId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists() && !forceFail) {
                        System.out.println("Post saved successfully");
                        result.complete(new Result<>(task.getResult().getValue(Post.class), true));
                    } else {
                        result.complete(new Result<>(null, false, "Error occurred, post may not exist under id " + postId));
                    }
                });
        return result;
    }

    @Override
    public CompletableFuture<Result<List<Post>>> fetchAllPosts(String userId) {
        CompletableFuture<Result<List<Post>>> result = new CompletableFuture<>();
        FirebaseDatabase.getInstance().getReference().child(POSTS).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                System.out.println("Post fetched successfully");
                DataSnapshot dataSnapshot = task.getResult();
                List<Post> postList = StreamSupport.stream(dataSnapshot.getChildren().spliterator(), false)
                        .map(childSnapshot -> childSnapshot.getValue(Post.class))
                        .collect(Collectors.toList());
                result.complete(new Result<>(postList, true));
            } else {
                result.complete(new Result<>(null, false, "Post fetch failed"));
            }
        });
        return result;
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
