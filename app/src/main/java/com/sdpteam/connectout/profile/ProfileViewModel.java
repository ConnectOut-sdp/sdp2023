package com.sdpteam.connectout.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {
    public ProfileDirectory mModel;

    public ProfileViewModel(ProfileDirectory mModel) {
        this.mModel = mModel;
    }

    /**
     * Get your own Profile
     */
    public LiveData<Profile> getValue(String uid) {
        return mModel.fetchProfile(uid);
    }

    /**
     * Save your new Profile
     */
    public void saveValue(Profile profile) {
        mModel.saveProfile(profile);
    }
}

