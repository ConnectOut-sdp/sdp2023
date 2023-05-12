package com.sdpteam.connectout.post.model;

import java.util.List;
import java.util.Objects;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.TimeoutException;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.FirebaseDatabase;
import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.EventFirebaseDataSource;
import com.sdpteam.connectout.event.nearbyEvents.filter.EventParticipantIdFilter;
import com.sdpteam.connectout.event.nearbyEvents.filter.ProfilesFilter;
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
                        final Post post = task.getResult().getValue(Post.class);
                        System.out.println("Post fetched successfully " + post.toString());
                        CompletableFuture<Event> futureEvent = new EventFirebaseDataSource().getEvent(post.getEventId());
                        Event event = fJoin(futureEvent);
                        if (event != null) {
                            if (post.getVisibility().equals(Post.PostVisibility.PUBLIC)) {
                                result.complete(new Result<>(post, true));
                            } else if (post.getVisibility().equals(Post.PostVisibility.SEMIPRIVATE) &&
                                    event.getOrganizer().equals(post.getProfileId()) || event.getParticipants().contains(post.getProfileId())) {
                                result.complete(new Result<>(post, true));
                            } else {
                                result.complete(new Result<>(null, false, "Error occurred, user has not access to this post"));
                            }
                        } else {
                            result.complete(new Result<>(null, false, "Error timeout occurred, the event of the post may no exist " +
                                    "and thus we cannot check if the user is allowed to access this post (eventId " + post.getEventId() + ")"));
                        }
                    } else {
                        result.complete(new Result<>(null, false, "Error occurred, post may not exist under id " + postId));
                    }
                });
        return result;
    }

    @Override
    public CompletableFuture<Result<List<Post>>> fetchAllPosts(String userId) {
        return eventIdsUserCanAccess(userId).thenCompose(allEventsUserCanAccess -> {
            final CompletableFuture<Result<List<Post>>> result = new CompletableFuture<>();

            FirebaseDatabase.getInstance().getReference().child(POSTS).get().addOnCompleteListener(task -> {
                if (task.isSuccessful() && task.getResult().exists() && !forceFail) {
                    final DataSnapshot dataSnapshot = task.getResult();
                    final List<Post> postList = StreamSupport.stream(dataSnapshot.getChildren().spliterator(), false)
                            .map(childSnapshot -> childSnapshot.getValue(Post.class))
                            .filter(Objects::nonNull)
                            .filter(post -> {
                                if (post.getVisibility() != null && post.getVisibility().equals(Post.PostVisibility.SEMIPRIVATE) && post.getEventId() != null) {
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

    private static CompletableFuture<List<String>> eventIdsUserCanAccess(String userId) {
        final EventParticipantIdFilter organizerOrParticipant = new EventParticipantIdFilter(userId);

        return new EventFirebaseDataSource()
                .getEventsByFilter(organizerOrParticipant, ProfilesFilter.NONE)
                .thenApply(events -> events.stream().map(Event::getId).collect(Collectors.toList()));
    }

    @Override
    public CompletableFuture<Result<List<Post>>> fetchAllPostsOfEvent(String userId, String eventId) {
        return fetchAllPosts(userId).thenApply(listResult -> {
            if (listResult.isSuccess()) {
                List<Post> postStream = listResult.value().stream()
                        .filter(post -> post.getEventId().equals(eventId))
                        .collect(Collectors.toList());

                return new Result<>(postStream, true);
            } else {
                return listResult;
            }
        });
    }

    @Override
    public CompletableFuture<Result<List<Post>>> fetchPostMadeByUser(String userId, String authorId) {
        return fetchAllPosts(userId).thenApply(listResult -> {
            if (listResult.isSuccess()) {
                List<Post> postStream = listResult.value().stream()
                        .filter(post -> post.getProfileId().equals(authorId))
                        .collect(Collectors.toList());
                return new Result<>(postStream, true);
            } else {
                return listResult;
            }
        });
    }

    private static <E> E fJoin(CompletableFuture<E> future) {
        try {
            return future.get(5, TimeUnit.SECONDS);
        } catch (ExecutionException | InterruptedException | TimeoutException e) {
            return null;
        }
    }
}
