package com.sdpteam.connectout.profile;

import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.AuthenticatedUser;
import com.sdpteam.connectout.authentication.GoogleAuth;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Once the Profile Activity has been created, this activity can be deleted and the intent in
 * EditProfileActivity must be adapted
 */
public class ProfileActivity extends AppCompatActivity {

    private final ProfileViewModel pvm = new ProfileViewModel(new ProfileFirebaseDataSource());

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

        Button rateProfile = findViewById(R.id.buttonRateProfile);
        Button editProfile = findViewById(R.id.buttonEditProfile);

        if (id != null) {
            // public user profile
            userProfile = pvm.getProfile(id).getValue();

            editProfile.setVisibility(View.INVISIBLE);
            rateProfile.setOnClickListener(v -> goToProfileRate(id));
        } else {
            // current user profile
            userProfile = pvm.getProfile(uid).getValue();
            //TODO : what is going on here?
            // pvm.saveProfile(userProfile);

            // getting the elements references
            rateProfile.setVisibility(View.INVISIBLE);
            editProfile.setOnClickListener(v -> goToEditProfile());
        }

        TextView name = findViewById(R.id.profileName);
        TextView email = findViewById(R.id.profileEmail);
        TextView bio = findViewById(R.id.profileBio);
        TextView gender = findViewById(R.id.profileGender);

        // setting the fetched data
        if (userProfile != null) {
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

    private void goToProfileRate(String id) {
        Intent intent = new Intent(ProfileActivity.this, ProfileRateActivity.class);
        intent.putExtra("uid", id);
        startActivity(intent);
    }
}