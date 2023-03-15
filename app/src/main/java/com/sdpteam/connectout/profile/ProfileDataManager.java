package com.sdpteam.connectout.profile;

import androidx.lifecycle.LiveData;

public interface ProfileDataManager {

    /**
     * saves the given Profile in the firebase database
     */
    void saveValue(Profile profile, String uid);

    /**
     * Fetches one's profile from the firebase database
     */
    LiveData<Profile> getProfile(String uid);
}
