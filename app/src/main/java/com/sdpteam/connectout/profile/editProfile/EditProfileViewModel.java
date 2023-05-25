package com.sdpteam.connectout.profile.editProfile;

import static com.sdpteam.connectout.profile.Profile.Gender;
import static com.sdpteam.connectout.profile.Profile.NULL_USER;

import com.sdpteam.connectout.authentication.AuthenticatedUser;
import com.sdpteam.connectout.authentication.GoogleAuth;
import com.sdpteam.connectout.profile.Profile;

import android.net.Uri;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

/**
 * ViewModel for editing a profile
 * It is used to edit a profile or create a new one
 * When opening the view it will prefill info if the profile already exists
 * (if first time then only the login info name and email can be prefilled otherwise everything available is showed)
 * It handles errors, thus the view only needs to observe the error message and progress to display them
 */
public class EditProfileViewModel extends ViewModel {

    private final EditProfile registration;
    private final Profile initialProfile;
    private final Profile profile;
    private final MutableLiveData<Boolean> progress;
    private final MutableLiveData<String> errorMessage;

    /**
     * @param editProfile    the model that allows to upload initialProfile picture and save new initialProfile info
     * @param initialProfile existing initialProfile we want to edit, null if want to create for the first time a new initialProfile
     */
    public EditProfileViewModel(EditProfile editProfile, Profile initialProfile) {
        final AuthenticatedUser user = new GoogleAuth().loggedUser();
        final AuthenticatedUser authenticatedUser = user == null ? new AuthenticatedUser(NULL_USER, "", "") : user;
        final Profile newProfile = new Profile(authenticatedUser.uid, authenticatedUser.name, authenticatedUser.email, "", Gender.MALE, 0, 0, null);

        this.registration = editProfile;
        this.initialProfile = initialProfile;
        this.profile = initialProfile == null ? newProfile : initialProfile;
        this.progress = new MutableLiveData<>(false);
        this.errorMessage = new MutableLiveData<>("");
    }

    /**
     * @return true if the user is creating a new profile, false if editing an existing one
     */
    public Boolean isFirstTimeFillingProfile() {
        return initialProfile == null;
    }

    /**
     * @return the profile that is being edited
     */
    public Profile profile() {
        return profile;
    }

    /**
     * @return boolean indicating if the operation is in progress (saving the image and then the profile)
     */
    public MutableLiveData<Boolean> getProgress() {
        return progress;
    }

    /**
     * @return error message to display to the user
     */
    public MutableLiveData<String> getErrorMessage() {
        return errorMessage;
    }

    /**
     * Final step of the registration (save Profile info)
     * Then, display a message to the user and stop the progress
     *
     * @param name         name of the user
     * @param email        email of the user
     * @param bio          bio of the user
     * @param g            gender of the user
     * @param profileImage image url of the user's profile image
     */
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