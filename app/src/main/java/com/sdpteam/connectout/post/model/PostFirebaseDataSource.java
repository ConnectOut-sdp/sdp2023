package com.sdpteam.connectout.post.model;

import static com.sdpteam.connectout.post.model.Post.PostVisibility.PUBLIC;
import static com.sdpteam.connectout.post.model.Post.PostVisibility.SEMIPRIVATE;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.EventFirebaseDataSource;
import com.sdpteam.connectout.event.nearbyEvents.filter.EventParticipantIdFilter;
import com.sdpteam.connectout.utils.Result;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

public class PostFirebaseDataSource implements PostDataSource {
    public static boolean FORCE_FAIL = false; // workaround because Mocking the isSuccessful() of the Task class is not possible
    // (MockitoException: Mockito cannot mock this class: class com.google.android.gms.tasks.Task)

    private static final String POSTS = "Posts";

    @Override
    public CompletableFuture<Result<String>> savePost(Post post) {
        if (post.getId() == null) {
            String newId = FirebaseDatabase.getInstance().getReference().child(POSTS).push().getKey();
            post = new Post(newId, post.getProfileId(), post.getEventId(), post.getCommentsChatId(), post.getImagesUrls(), post.getNbrLikes(), post.getVisibility(), post.getTitle(),
                    post.getDescription());
        }
        final Post finalPost = post;

        CompletableFuture<Result<String>> result = new CompletableFuture<>();

        FirebaseDatabase.getInstance().getReference().child(POSTS).child(post.getId()).setValue(post)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && !FORCE_FAIL) {
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
                    if (task.isSuccessful() && !FORCE_FAIL) {
                        result.complete(new Result<>(postId, true));
                    } else {
                        result.complete(new Result<>(null, false, "Post save failed"));
                    }
                });
        return result;
    }

    @Override
    public CompletableFuture<Result<Post>> fetchPost(String postId) {
        final CompletableFuture<Result<Post>> result = new CompletableFuture<>();
        final CompletableFuture<Result<Post>> subStep = new CompletableFuture<>();

        FirebaseDatabase.getInstance().getReference().child(POSTS).child(postId).get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful() && task.getResult().exists() && !FORCE_FAIL) {
                        final Post post = task.getResult().getValue(Post.class);
                        System.out.println("Post fetched successfully " + post.toString());
                        subStep.complete(new Result<>(post, true));
                    } else {
                        subStep.complete(new Result<>(null, false, "Error occurred, post may not exist under id " + postId));
                    }
                });

        // checking if the user has access to this post :
        subStep.thenAcceptAsync(postResult -> {
            if (!postResult.isSuccess()) {
                result.complete(postResult);
                return;
            }
            final Post post = Objects.requireNonNull(postResult.value());

            if (post.getVisibility() == null) {
                result.complete(new Result<>(null, false, "Event has visibility set to NULL which is not expected"));
            } else if (post.getVisibility().equals(PUBLIC)) {
                result.complete(new Result<>(post, true));
            } else if (post.getVisibility().equals(SEMIPRIVATE)) {
                final Event event = (new EventFirebaseDataSource().getEvent(post.getEventId())).join();
                if (event == null) {
                    result.complete(new Result<>(null, false,
                            "Error the event associated to the post does not exist! (maybe a timeout issue) postId" + postId + " eventId" + post.getEventId()));
                } else {
                    if (event.getOrganizer().equals(post.getProfileId()) || event.getParticipants().contains(post.getProfileId())) {
                        result.complete(new Result<>(post, true));
                    } else {
                        result.complete(new Result<>(null, false, "Error occurred, user has not access to this post"));
                    }
                }
            }
        }).join();

        return result;
    }

    @Override
    public CompletableFuture<Result<List<Post>>> fetchAllPosts(String userId) {
        return eventIdsUserCanAccess(userId).thenCompose(allEventsUserCanAccess -> {
            final CompletableFuture<Result<List<Post>>> result = new CompletableFuture<>();

            FirebaseDatabase.getInstance().getReference().child(POSTS).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists() && !FORCE_FAIL) {
                    final DataSnapshot dataSnapshot = task.getResult();
                    final List<Post> postList = StreamSupport.stream(dataSnapshot.getChildren().spliterator(), false)
                            .map(childSnapshot -> childSnapshot.getValue(Post.class))
                            .filter(Objects::nonNull)
                            .filter(post -> {
                                if (post.isSemiPrivate()) {
                                    return allEventsUserCanAccess.contains(post.getEventId());
                                } else {
                                    return true;
                                }
                            })
                            .collect(Collectors.toList());
                    result.complete(new Result<>(postList, true));
                } else {
                    result.complete(new Result<>(null, false, "Post fetch failed"));
                }
            });
            return result;
        });
    }

    private CompletableFuture<List<String>> eventIdsUserCanAccess(String userId) {
        final EventParticipantIdFilter organizerOrParticipant = new EventParticipantIdFilter(userId);

        return new EventFirebaseDataSource()
                .getEventsByFilter(organizerOrParticipant)
                .thenApply(events -> events.stream().map(Event::getId).collect(Collectors.toList()));
    }

    @Override
    public CompletableFuture<Result<List<Post>>> fetchAllPostsOfEvent(String userId, String eventId) {
        return fetchAllPostsFiltered(userId, post -> post.getEventId().equals(eventId));
    }

    @Override
    public CompletableFuture<Result<List<Post>>> fetchPostMadeByUser(String userId, String authorId) {
        return fetchAllPostsFiltered(userId, post -> post.getProfileId().equals(authorId));
    }

    private CompletableFuture<Result<List<Post>>> fetchAllPostsFiltered(String userId, Predicate<Post> postFilter) {
        return fetchAllPosts(userId).thenApply(listResult -> {
            if (listResult.isSuccess() && !FORCE_FAIL) {
                List<Post> postStream = listResult.value().stream()
                        .filter(postFilter)
                        .collect(Collectors.toList());

                return new Result<>(postStream, true);
            } else {
                return listResult;
            }
        });
    }
}
