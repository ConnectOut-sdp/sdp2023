package com.sdpteam.connectout.registration;

import static com.sdpteam.connectout.profile.Profile.Gender;
import static com.sdpteam.connectout.registration.CompleteRegistration.MandatoryFields;

import com.sdpteam.connectout.authentication.Authentication;

import android.net.Uri;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class RegistrationViewModel extends ViewModel {

    private final CompleteRegistration registration;
    private final Authentication auth;

    private final MutableLiveData<Boolean> progress;
    private final MutableLiveData<String> errorMessage;

    public RegistrationViewModel(CompleteRegistration registration, Authentication auth) {
        this.registration = registration;
        this.auth = auth;
        this.progress = new MutableLiveData<>(false);
        this.errorMessage = new MutableLiveData<>("");
    }

    String currentName() {
        return auth.isLoggedIn() ? auth.loggedUser().name : "";
    }

    String currentEmail() {
        return auth.isLoggedIn() ? auth.loggedUser().email : "";
    }

    public MutableLiveData<Boolean> getProgress() {
        return progress;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void completeRegistration(String name, String email, String bio, Gender g, Uri profileImage) {
        progress.setValue(true);
        if (auth.isLoggedIn()) {
            if (profileImage == null) {
                updateProfileInfos(name, email, bio, g, "");
            } else {
                registration.uploadProfile(profileImage).thenAccept(profileImageUrl -> {
                    if (profileImageUrl == null) {
                        displayFinishWithMessage("Error uploading profile image");
                    } else {
                        updateProfileInfos(name, email, bio, g, profileImageUrl.toString());
                    }
                });
            }
        } else {
            displayFinishWithMessage("Cannot complete the registration you're not even logged in.");
        }
    }

    private void updateProfileInfos(String name, String email, String bio, Gender g, String profileImageUrl) {
        registration.completeRegistration(auth.loggedUser().uid, new MandatoryFields(name, email, bio, g), profileImageUrl).thenAccept(isSuccess -> {
            displayFinishWithMessage(isSuccess ? "Operation successful" : "Error while completing the registration");
        });
    }

    private void displayFinishWithMessage(String msg) {
        progress.setValue(false);
        errorMessage.setValue(msg);
    }
}