package com.sdpteam.connectout.registration;

import static com.sdpteam.connectout.profile.Profile.Gender;

import com.sdpteam.connectout.authentication.Authentication;
import com.sdpteam.connectout.authentication.GoogleAuth;
import com.sdpteam.connectout.profile.ProfileFirebaseDataSource;

import androidx.lifecycle.ViewModel;

public class RegistrationViewModel extends ViewModel {

    private CompleteRegistration registration;
    private Authentication auth;

    public RegistrationViewModel() {
        final ProfileFirebaseDataSource firebaseProfiles = new ProfileFirebaseDataSource();
        registration = new CompleteRegistration(firebaseProfiles);
        auth = new GoogleAuth();
    }

    String currentName() {
        return auth.loggedUser().name;
    }

    String currentEmail() {
        return auth.loggedUser().email;
    }

    public void completeRegistration(String name, String email, String bio, Gender g) {
        if (auth.isLoggedIn()) {
            registration.completeRegistration(auth.loggedUser().uid, name, email, bio, g);
        } else {
            throw new IllegalStateException("Cannot complete the registration you're not even logged in.");
        }
    }

    // for testing (mocking)
    public void setRegistration(CompleteRegistration registration) {
        this.registration = registration;
    }

    // for testing (mocking)
    public void setAuth(Authentication auth) {
        this.auth = auth;
    }
}