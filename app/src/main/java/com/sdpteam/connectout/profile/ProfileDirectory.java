package com.sdpteam.connectout.profile;

import androidx.lifecycle.LiveData;

public interface ProfileDirectory {

    /**
     * saves the given Profile in the firebase database
     */
    void saveProfile(Profile profile);

    /**
     * Fetches one's profile from the firebase database
     */
    LiveData<Profile> fetchProfile(String uid);
}
