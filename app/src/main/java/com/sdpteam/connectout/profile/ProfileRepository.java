package com.sdpteam.connectout.profile;

import java.util.concurrent.CompletableFuture;

public interface ProfileRepository {

    /**
     * saves the given Profile in the repository
     */
    void saveProfile(Profile profile);

    /**
     * Fetches one's profile from the repository
     */
    CompletableFuture<Profile> fetchProfile(String uid);
}
