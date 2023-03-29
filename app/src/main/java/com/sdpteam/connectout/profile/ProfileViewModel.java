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

    /**
     * Update rating of a Profile
     */
    public void updateRating(String uid, double newRating) {
        Profile userProfile = this.getProfile(uid).getValue();
        assert userProfile != null;
        double rating = userProfile.getRating();
        int numRatings = userProfile.getNumRatings();

        //compute new rating
        rating = (rating * numRatings + newRating) / (numRatings + 1);
        numRatings++;

        //save new rating
        this.saveProfile(
                new Profile(uid, userProfile.getName(), userProfile.getEmail(),
                        userProfile.getBio(), userProfile.getGender(), rating, numRatings
        ));
    }
}

