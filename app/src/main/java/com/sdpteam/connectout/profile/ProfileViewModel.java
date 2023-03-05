package com.sdpteam.connectout.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {
    public IntProfileModel mModel;

    public ProfileViewModel(IntProfileModel mModel) {
        this.mModel = mModel;
    }

    public LiveData<Profile> getValue() {
        return mModel.getValue();
    }

    public void saveValue(Profile profile) {
        mModel.saveValue(profile);
    }
}

