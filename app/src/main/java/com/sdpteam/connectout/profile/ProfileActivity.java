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

        Profile userProfile;

        // fetch data
        AuthenticatedUser au = new GoogleAuth().loggedUser();
        // id public id
        String id = getIntent().getStringExtra("id");
        // user id
        String uid = (au == null) ? NULL_USER : au.uid;

        if(id != null) {
            // public user profile
            userProfile = pvm.getProfile(id).getValue();
        } else {
            // current user profile
            userProfile = pvm.getProfile(uid).getValue();
            pvm.saveValue(userProfile, uid);

            // getting the elements references
            Button editProfile = findViewById(R.id.buttonEditProfile);
            editProfile.setOnClickListener(v -> goToEditProfile());
        }

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
    }

    private void goToEditProfile() {
        Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
        startActivity(intent);
        finish();
    }
}