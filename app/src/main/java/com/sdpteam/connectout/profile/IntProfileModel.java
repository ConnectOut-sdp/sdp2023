package com.sdpteam.connectout.profile;

import androidx.lifecycle.LiveData;

public interface IntProfileModel {
    void saveValue(Profile profile);
    LiveData<Profile> getValue();
}
