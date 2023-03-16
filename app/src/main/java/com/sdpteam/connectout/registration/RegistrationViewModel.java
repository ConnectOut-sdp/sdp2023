package com.sdpteam.connectout.registration;

import static com.sdpteam.connectout.profile.Profile.Gender;
import static com.sdpteam.connectout.registration.CompleteRegistration.MandatoryFields;

import com.sdpteam.connectout.authentication.Authentication;

import androidx.lifecycle.ViewModel;

public class RegistrationViewModel extends ViewModel {

    private CompleteRegistration registration;
    private Authentication auth;

    public RegistrationViewModel(CompleteRegistration registration, Authentication auth) {
        this.registration = registration;
        this.auth = auth;
    }

    String currentName() {
        return auth.loggedUser().name;
    }

    String currentEmail() {
        return auth.loggedUser().email;
    }

    public void completeRegistration(String name, String email, String bio, Gender g) {
        if (auth.isLoggedIn()) {
            registration.completeRegistration(auth.loggedUser().uid, new MandatoryFields(name, email, bio, g));
        } else {
            throw new IllegalStateException("Cannot complete the registration you're not even logged in.");
        }
    }
}