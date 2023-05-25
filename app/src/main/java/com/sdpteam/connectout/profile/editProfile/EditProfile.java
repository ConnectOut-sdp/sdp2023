package com.sdpteam.connectout.profile.editProfile;

import android.net.Uri;

import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileDataSource;
import com.sdpteam.connectout.remoteStorage.FileStorageFirebase;

import java.util.concurrent.CompletableFuture;

public class EditProfile {
    private final ProfileDataSource profiles;
    private final FileStorageFirebase imageStorageFirebase;

    public EditProfile(ProfileDataSource profiles) {
        this.profiles = profiles;
        imageStorageFirebase = new FileStorageFirebase();
    }

    /**
     * @param profileFile image (jpg) the user selected from his phone and wants to upload
     * @return url to where this image has been uploaded
     */
    public CompletableFuture<Uri> uploadProfile(Uri profileFile) {
        CompletableFuture<Uri> profileImageUrl = new CompletableFuture<>();
        imageStorageFirebase.uploadFile(profileFile, "jpg").thenAccept(profileImageUrl::complete);
        return profileImageUrl;
    }

    /**
     * CSaving the profile
     */
    public CompletableFuture<Boolean> saveProfile(Profile updatedProfile) {
        return profiles.saveProfile(updatedProfile);
    }
}
