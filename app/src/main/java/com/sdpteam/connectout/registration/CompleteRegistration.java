package com.sdpteam.connectout.registration;

import static com.sdpteam.connectout.profile.Profile.Gender;

import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileDirectory;

public class CompleteRegistration {
    private final ProfileDirectory profiles;

    public CompleteRegistration(ProfileDirectory profiles) {
        this.profiles = profiles;
    }

    /**
     * Creating the initial profile metadata for ConnectOut
     */
    public void completeRegistration(String userId, MandatoryFields completion) {
        final double defaultRating = 0.0;
        final int defaultNumRatings = 0;
        Profile initialProfile = new Profile(userId, completion.name, completion.email, completion.bio, completion.g, defaultRating, defaultNumRatings);
        profiles.saveProfile(initialProfile);
    }

    /**
     * Fields that must be provided for completing the registration
     */
    public static class MandatoryFields {
        String name;
        String email;
        String bio;
        Gender g;

        public MandatoryFields(String name, String email, String bio, Gender g) {
            this.name = name;
            this.email = email;
            this.bio = bio;
            this.g = g;
        }
    }
}
