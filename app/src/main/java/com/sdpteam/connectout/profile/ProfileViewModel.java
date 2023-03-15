package com.sdpteam.connectout.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {
    public ProfileDataManager model;

    public ProfileViewModel(ProfileDataManager model) {
        this.model = model;
    }

    /**
     * Get your own Profile
     */
    public LiveData<Profile> getProfile(String uid) {
        return model.getProfile(uid);
    }

    /**
     * Save your new Profile
     */
    public void saveProfile(Profile profile, String uid) {
        model.saveProfile(profile, uid);
    }
}

