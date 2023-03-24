package com.sdpteam.connectout.profile;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class ProfileViewModel extends ViewModel {
    public ProfileDirectory profileDirectory;

    public ProfileViewModel(ProfileDirectory profileDirectory) {
        this.profileDirectory = profileDirectory;
    }

    /**
     * Get a Profile
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

