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

    private ProfileViewModel pvm = new ProfileViewModel(new ProfileModel());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        // fetch data
        AuthenticatedUser au = new GoogleAuth().loggedUser();
        //get new values
        String uid = (au == null) ? NULL_USER : au.uid;
        Profile userProfile = pvm.getValue(uid).getValue();

        // getting the elements references
        Button editProfile = findViewById(R.id.buttonEditProfile);
        TextView nameTV = findViewById(R.id.textViewName);
        TextView emailTV = findViewById(R.id.textViewEmail);
        TextView bioTV = findViewById(R.id.textViewBio);
        TextView genderTV = findViewById(R.id.textViewGender);

        // setting the fetched data
        nameTV.setText(userProfile.getName());
        emailTV.setText(userProfile.getEmail());
        bioTV.setText(userProfile.getBio());
        genderTV.setText(userProfile.getGender().name());

        editProfile.setOnClickListener(v -> {
            goToEditProfile(userProfile);
        });
    }


    private void goToEditProfile(Profile p) {
        Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
        startActivity(intent);
        finish();
    }
}