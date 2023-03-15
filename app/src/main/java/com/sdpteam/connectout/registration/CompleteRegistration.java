package com.sdpteam.connectout.registration;

import static com.sdpteam.connectout.profile.Profile.Gender;

import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileDataManager;

public class CompleteRegistration {
    private final ProfileDataManager profiles;

    public CompleteRegistration(ProfileDataManager profiles) {
        this.profiles = profiles;
    }

    /**
     * Creating the initial profile metadata for ConnectOut
     */
    public void completeRegistration(String userId, String name, String email, String bio, Gender g) {
        Profile initialProfile = new Profile(userId, name, email, bio, g, 0, 0);
        profiles.saveValue(initialProfile);
    }
}
