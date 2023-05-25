package com.sdpteam.connectout.profile.editProfile;

import static com.sdpteam.connectout.profile.Profile.Gender;

import com.sdpteam.connectout.authentication.AuthenticatedUser;
import com.sdpteam.connectout.authentication.GoogleAuth;
import com.sdpteam.connectout.profile.Profile;

import android.net.Uri;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EditProfileViewModel extends ViewModel {

    private final EditProfile registration;
    private final Profile initialProfile;
    private final Profile profile;
    private final MutableLiveData<Boolean> progress;
    private final MutableLiveData<String> errorMessage;

    /**
     * @param registration   the model that allows to upload initialProfile picture and save new initialProfile info
     * @param initialProfile existing initialProfile we want to edit, null if want to create for the first time a new initialProfile
     */
    public EditProfileViewModel(EditProfile registration, Profile initialProfile) {
        final AuthenticatedUser user = new GoogleAuth().loggedUser();
        final AuthenticatedUser authenticatedUser = user == null ? new AuthenticatedUser("", "", "") : user;
        final Profile newProfile = new Profile(authenticatedUser.uid, authenticatedUser.name, authenticatedUser.email, "", Gender.MALE, 0, 0, null);

        this.registration = registration;
        this.initialProfile = initialProfile;
        this.profile = initialProfile == null ? newProfile : initialProfile;
        this.progress = new MutableLiveData<>(false);
        this.errorMessage = new MutableLiveData<>("");
    }

    public Boolean isFirstTimeFillingProfile() {
        return initialProfile == null;
    }

    public Profile profile() {
        return profile;
    }

    public MutableLiveData<Boolean> getProgress() {
        return progress;
    }

    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    public void saveNewProfile(String name, String email, String bio, Gender g, Uri profileImage) {
        progress.setValue(true);
        if (profileImage == null) {
            updateProfileInfo(name, email, bio, g, "");
        } else {
            registration.uploadProfile(profileImage).thenAccept(profileImageUrl -> {
                if (profileImageUrl == null) {
                    displayFinishWithMessage("Error uploading profile image");
                } else {
                    updateProfileInfo(name, email, bio, g, profileImageUrl.toString());
                }
            });
        }
    }

    private void updateProfileInfo(String name, String email, String bio, Gender g, String profileImageUrl) {
        final Profile updated = new Profile(profile.getId(), name, email, bio, g, profile.getRating(), profile.getNumRatings(), profileImageUrl);
        registration.saveProfile(updated).thenAccept(isSuccess -> {
            displayFinishWithMessage(isSuccess ? "Operation successful" : "Error while completing the registration");
        });
    }

    private void displayFinishWithMessage(String msg) {
        errorMessage.setValue(msg);
        progress.setValue(false);
    }
}