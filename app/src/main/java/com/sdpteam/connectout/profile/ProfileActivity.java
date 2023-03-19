package com.sdpteam.connectout.profile;

import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;

import com.sdpteam.connectout.PassedField;
import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.AuthenticatedUser;
import com.sdpteam.connectout.authentication.GoogleAuth;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Once the Profile Activity has been created, this activity can be deleted and the intent in
 * EditProfileActivity must be adapted
 */
public class ProfileActivity extends AppCompatActivity {

    private final ProfileViewModel pvm = new ProfileViewModel(new ProfileModel());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Profile userProfile;

        //Fetch current user.
        AuthenticatedUser au = new GoogleAuth().loggedUser();
        
        //Find Ids with which activity was launched.
        String id = getIntent().getStringExtra(PassedField.UserId.toString());

        String uid = (au == null) ? NULL_USER : au.uid;

        //If given is is null, or not the current user id.
        if (id != null) {
            //Fetch & display a non modifiable profile.
            userProfile = pvm.getProfile(id).getValue();
        } else {
            //Fetch & display the user's modifiable profile.
            userProfile = pvm.getProfile(uid).getValue();

            //Launch the modifiable profile.
            Button editProfile = findViewById(R.id.buttonEditProfile);
            editProfile.setOnClickListener(v -> goToEditProfile());
        }

        // If view retrieved is not null, Show it.
        if (userProfile != null) {
            setUpProfileView(userProfile);
        }
    }

    /**
     * Current user goes to editing profile activity.
     */
    private void goToEditProfile() {
        Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
        startActivity(intent);
    }

    /**
     * Displays the given profile on screen.
     * @param profile (Profile): given profile to display.
     */
    private void setUpProfileView(Profile profile){
        TextView name = findViewById(R.id.profileName);
        TextView email = findViewById(R.id.profileEmail);
        TextView bio = findViewById(R.id.profileBio);
        TextView gender = findViewById(R.id.profileGender);
        
        name.setText(profile.getName());
        email.setText(profile.getEmail());
        bio.setText(profile.getBio());
        gender.setText(profile.getGender().name());
    }
}