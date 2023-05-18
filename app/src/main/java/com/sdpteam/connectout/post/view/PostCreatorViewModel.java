package com.sdpteam.connectout.post.view;

import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

import com.sdpteam.connectout.post.model.Post;
import com.sdpteam.connectout.post.model.Post.PostVisibility;
import com.sdpteam.connectout.post.model.PostDataSource;
import com.sdpteam.connectout.remoteStorage.FileStorageFirebase;
import com.sdpteam.connectout.utils.Result;

import android.net.Uri;
import androidx.lifecycle.MutableLiveData;

public class PostCreatorViewModel {
    private final PostDataSource postDataSource;
    private final String user;
    private final MutableLiveData<Result<String>> statusMsg;

    public PostCreatorViewModel(PostDataSource postDataSource, String currentUserId) {
        this.postDataSource = postDataSource;
        this.user = currentUserId;
        statusMsg = new MutableLiveData<>();
    }

    public MutableLiveData<Result<String>> statusMsgLiveData() {
        return statusMsg;
    }

    public void createPost(String eventId, String title, String desc, boolean isPublic, List<Uri> images) {
        final PostVisibility postVisibility = isPublic ? PostVisibility.PUBLIC : PostVisibility.SEMIPRIVATE;

        Post tmpPost = new Post(null, user, eventId, "", null, 0, postVisibility, title, desc);

        List<Uri> localImagesUris = images.stream().filter(Objects::nonNull).collect(Collectors.toList());
        uploadImages(localImagesUris, tmpPost);
    }

    private void uploadImages(List<Uri> localUris, Post tmpPost) {
        new FileStorageFirebase().uploadImages(localUris).thenAccept(uris -> {

            if (!uris.stream().allMatch(Objects::nonNull)) {
                statusMsg.setValue(new Result<>(null, false, "Error while uploading images"));
            } else {
                finishPostCreation(uris, tmpPost);
            }
        });
    }

    private void finishPostCreation(List<Uri> uris, Post tmpPost) {
        final List<String> imageUrls = uris.stream().map(Uri::toString).collect(Collectors.toList());

        final Post finalPost = new Post(tmpPost.getId(),
                tmpPost.getProfileId(),
                tmpPost.getEventId(),
                "",
                imageUrls,
                tmpPost.getNbrLikes(),
                tmpPost.getVisibility(),
                tmpPost.getTitle(),
                tmpPost.getDescription());

        postDataSource.savePost(finalPost).thenAccept(postIdResult -> {
            if (postIdResult.isSuccess()) {
                statusMsg.setValue(new Result<>(postIdResult.value(), true, "Post created successfully"));
            } else {
                statusMsg.setValue(new Result<>(null, false, imageUrls.size() + " images uploaded successfully, but post creation failed!"));
            }
        });
    }
}
