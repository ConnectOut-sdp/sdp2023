package com.sdpteam.connectout.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {
    public ProfileDataManager mModel;

    public ProfileViewModel(ProfileDataManager mModel) {
        this.mModel = mModel;
    }

    /**
     * Get your own Profile
     */
    public LiveData<Profile> getProfile(String uid) {
        return mModel.getProfile(uid);
    }

    /**
     * Save your new Profile
     */
    public void saveValue(Profile profile, String uid) {
        mModel.saveValue(profile, uid);
    }
}

