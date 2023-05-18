package com.sdpteam.connectout.post.view;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.sdpteam.connectout.post.model.Post;
import com.sdpteam.connectout.post.model.PostDataSource;
import com.sdpteam.connectout.utils.Result;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import io.reactivex.rxjava3.annotations.NonNull;
import io.reactivex.rxjava3.annotations.Nullable;

public class PostsViewModel extends ViewModel {
    private final PostDataSource postDataSource;
    private final MutableLiveData<List<Post>> postLiveData;
    private final MutableLiveData<String> errorMessage;
    private String lastUserId;
    private PostOption lastOption;
    private String lastOptionId;

    public PostsViewModel(PostDataSource postDataSource) {
        this.postDataSource = postDataSource;
        postLiveData = new MutableLiveData<>(new ArrayList<>());
        errorMessage = new MutableLiveData<>(null);
    }

    /**
     * Ask for an update with the last given options.
     */
    public void refreshPosts() {
        if (lastOption == null) {
            return;
        }
        switch (lastOption) {
            case EVENT:
                getEventPosts(lastUserId, lastOptionId);
                break;
            case AUTHORED:
                getAuthoredPosts(lastUserId, lastOptionId);
                break;
            case ALL:
                getAllPosts(lastUserId);
                break;
            default:
                break;
        }
    }

    /**
     * @return (LiveData < List < Post > >): the updating data of posts.
     */
    public LiveData<List<Post>> getPostsLiveData() {
        return postLiveData;
    }

    public LiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * @param userId   (String): user id of the current user.
     * @param eventId  (String): id of the event to fetch, or null if no specific event is being fetched.
     * @param authorId (String): id of the author from which fetch, or null if no specific event is being fetched.
     */
    public void getPosts(@NonNull String userId, @Nullable String eventId, @Nullable String authorId) {
        if (userId != null) {
            if (eventId != null) {
                getEventPosts(userId, eventId);
            } else if (authorId != null) {
                getAuthoredPosts(userId, authorId);
            } else {
                getAllPosts(userId);
            }
        }
    }

    /**
     * @param userId (String): current user for which we fetch all posts
     */
    private void getAllPosts(String userId) {
        lastUserId = userId;
        lastOption = PostOption.ALL;
        postDataSource.fetchAllPosts(userId).thenAccept(this::setOnResult);
    }

    /**
     * @param userId  (String): current user for which we fetch posts
     * @param eventId (String): id of the event we need to fetch posts from
     */
    private void getEventPosts(String userId, String eventId) {
        lastOption = PostOption.EVENT;
        lastOptionId = eventId;
        postDataSource.fetchAllPostsOfEvent(userId, eventId).thenAccept(this::setOnResult);
    }

    /**
     * @param userId   (String): current user for which we fetch posts
     * @param authorId (String): id of the user we need to fetch posts from
     */
    private void getAuthoredPosts(String userId, String authorId) {
        lastOption = PostOption.AUTHORED;
        lastOptionId = authorId;
        postDataSource.fetchPostMadeByUser(userId, authorId).thenAccept(this::setOnResult);
    }

    private void setOnResult(Result<List<Post>> result) {
        if (result.isSuccess()) {
            postLiveData.setValue(result.value());
        } else {
            errorMessage.setValue(result.msg());
        }
    }

    /**
     * Enumeration indicating the type of post we are using.
     */
    private enum PostOption {
        ALL,
        EVENT,
        AUTHORED
    }
}
