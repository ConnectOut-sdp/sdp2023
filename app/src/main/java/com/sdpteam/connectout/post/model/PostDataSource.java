package com.sdpteam.connectout.post.model;

import com.sdpteam.connectout.utils.Result;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface PostDataSource {

    /**
     * Create or update a post identified with Post.id
     * Note that if you want to create a new post, just provide a null id.
     *
     * @return a completable future of a Result (which says if it is successful or not)
     */
    CompletableFuture<Result<String>> savePost(Post post);

    /**
     * Delete a post identified with Post.id
     *
     * @param postId post id to be deleted
     * @return a completable future of a Result (which says if it is successful or not)
     */
    CompletableFuture<Result<String>> deletePost(String postId);

    /**
     * @param postId (String): id of the post to be fetched
     * @return a completable future of a Result. It is successful if the post is found, otherwise it is a failure.
     */
    CompletableFuture<Result<Post>> fetchPost(String postId);

    /**
     * All posts that some user is allowed to see
     * (posts with visibility PUBLIC if the user did not join the event)
     * (posts with visibility PUBLIC or SEMIPRIVATE  if the user joined the event)
     *
     * @param userId (String): profile id of the user that wants to look a posts
     * @return a completable future of a Result. It is successful if the posts are found, otherwise it is a failure.
     */
    CompletableFuture<Result<List<Post>>> fetchAllPosts(String userId);

    /**
     * All posts of an event that the user is allowed to see
     * (posts with visibility PUBLIC if the user did not join the event)
     * (posts with visibility PUBLIC or SEMIPRIVATE  if the user joined the event)
     *
     * @param userId  id of the user that wants to access to the posts of the event
     * @param eventId event id
     * @return
     */
    CompletableFuture<Result<List<Post>>> fetchAllPostsOfEvent(String userId, String eventId);

    /**
     * userId wants to see the posts of authorId. Only those that byUserId is allowed to see of course.
     * (posts with visibility PUBLIC if the user did not join the event)
     * (posts with visibility PUBLIC or SEMIPRIVATE  if the user joined the event)
     *
     * @param userId   id of the user that wants to access to the posts of the user
     * @param authorId id of the user whose posts are to be fetched
     * @return a completable future of a Result. It is successful if the posts are found, otherwise it is a failure.
     */
    CompletableFuture<Result<List<Post>>> fetchPostMadeByUser(String userId, String authorId);
}

