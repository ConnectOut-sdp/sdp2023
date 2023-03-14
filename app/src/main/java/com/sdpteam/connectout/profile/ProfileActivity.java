package com.sdpteam.connectout.profile;

import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.AuthenticatedUser;
import com.sdpteam.connectout.authentication.GoogleAuth;


/**
 * Once the Profile Activity has been created, this activity can be deleted and the intent in
 * EditProfileActivity must be adapted
 *
 * */
public class ProfileActivity extends AppCompatActivity {

    private final ProfileViewModel pvm = new ProfileViewModel(new ProfileModel());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // fetch data
        AuthenticatedUser au = new GoogleAuth().loggedUser();

        //get new values
        String uid = (au == null) ? NULL_USER : au.uid;
        Profile userProfile = pvm.getProfile(uid).getValue();
        pvm.saveValue(userProfile, uid);

        // maybe put them in the local cache

        // getting the elements references
        Button editProfile = findViewById(R.id.buttonEditProfile);
        TextView name = findViewById(R.id.profileName);
        TextView email = findViewById(R.id.profileEmail);
        TextView bio = findViewById(R.id.profileBio);
        TextView gender = findViewById(R.id.profileGender);

        // setting the fetched data
        if(userProfile != null) {
            name.setText(userProfile.getName());
            email.setText(userProfile.getEmail());
            bio.setText(userProfile.getBio());
            gender.setText(userProfile.getGender().name());
        }

        editProfile.setOnClickListener(v -> goToEditProfile());
    }

    private void goToEditProfile() {
        Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
        startActivity(intent);
        finish();
    }
}