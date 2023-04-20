package com.sdpteam.connectout.registration;

import static com.sdpteam.connectout.profile.Profile.Gender;

import java.util.concurrent.CompletableFuture;

import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileRepository;
import com.sdpteam.connectout.remoteStorage.FileStorageFirebase;

import android.net.Uri;

public class CompleteRegistration {
    private final ProfileRepository profiles;
    private final FileStorageFirebase imageStorageFirebase;

    public CompleteRegistration(ProfileRepository profiles) {
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
     * Creating the initial profile metadata for ConnectOut
     */
    public CompletableFuture<Boolean> completeRegistration(String userId, MandatoryFields completion, String profileImageUrl) {
        final double defaultRating = 0.0;
        final int defaultNumRatings = 0;
        Profile initialProfile = new Profile(userId, completion.name, completion.email, completion.bio, completion.g, defaultRating, defaultNumRatings, profileImageUrl);
        return profiles.saveProfile(initialProfile);
    }

    /**
     * Fields that must be provided for completing the registration
     */
    public static class MandatoryFields {
        String name;
        String email;
        String bio;
        Gender g;

        public MandatoryFields(String name, String email, String bio, Gender g) {
            this.name = name;
            this.email = email;
            this.bio = bio;
            this.g = g;
        }
    }
}
