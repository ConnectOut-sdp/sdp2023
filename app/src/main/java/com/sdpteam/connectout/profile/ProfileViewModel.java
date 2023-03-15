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
    public LiveData<Profile> getValue(String uid) {
        return model.getValue(uid);
    }

    /**
     * Save your new Profile
     */
    public void saveValue(Profile profile, String uid) {
        model.saveValue(profile, uid);
    }
}

