package com.sdpteam.connectout.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<Profile> profileLiveData;
    public ProfileRepository profileRepository;

    public ProfileViewModel(ProfileRepository profileRepository) {
        this.profileRepository = profileRepository;
        this.profileLiveData = new MutableLiveData<>();
    }

    /**
     * trigger a fetch from repo
     */
    public void fetchProfile(String uid) {
        profileRepository.fetchProfile(uid).thenAccept(profile -> {
            profileLiveData.setValue(profile);
        });
    }

    /**
     * Save your new Profile
     */
    public void saveProfile(Profile profile) {
        profileRepository.saveProfile(profile);
    }

    public LiveData<Profile> getProfileLiveData() {
        return profileLiveData;
    }
}

