package com.sdpteam.connectout.profile.editProfile;

import java.util.concurrent.CompletableFuture;

import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileDataSource;
import com.sdpteam.connectout.remoteStorage.FileStorageFirebase;

import android.net.Uri;

/**
 * It is the model that allows to upload profile picture and save new profile info
 * (the uploaded profile image url is then used as the profile image url field in the profile info)
 */
public class EditProfile {
    private final ProfileDataSource profiles;
    private final FileStorageFirebase imageStorageFirebase;

    /**
     * @param profileDataSource model that allows to save the profile info
     */
    public EditProfile(ProfileDataSource profileDataSource) {
        this.profiles = profileDataSource;
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
     * Saving the profile
     */
    public CompletableFuture<Boolean> saveProfile(Profile updatedProfile) {
        return profiles.saveProfile(updatedProfile);
    }
}
