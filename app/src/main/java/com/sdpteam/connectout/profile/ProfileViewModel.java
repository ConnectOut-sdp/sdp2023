package com.sdpteam.connectout.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {
    public ProfileDirectory profileDirectory;

    public ProfileViewModel(ProfileDirectory profileDirectory) {
        this.profileDirectory = profileDirectory;
    }

    /**
     * Get your own Profile
     */
    public LiveData<Profile> getProfile(String uid) {
        return profileDirectory.fetchProfile(uid);
    }

    /**
     * Save your new Profile
     */
    public void saveProfile(Profile profile) {
        profileDirectory.saveProfile(profile);
    }
}

