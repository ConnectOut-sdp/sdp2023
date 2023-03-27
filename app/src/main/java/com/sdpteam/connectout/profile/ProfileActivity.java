package com.sdpteam.connectout.profile;

import static android.view.View.VISIBLE;
import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.Authentication;
import com.sdpteam.connectout.authentication.GoogleAuth;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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
    Authentication auth = new GoogleAuth();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button editProfile = findViewById(R.id.buttonEditProfile);
        editProfile.setVisibility(View.INVISIBLE);

        String userIdToDisplay = getIntent().getStringExtra("uid");
        if (userIdToDisplay == null) {
            if (auth.isLoggedIn()) {
                userIdToDisplay = auth.loggedUser().uid;
                editProfile.setVisibility(VISIBLE);
                editProfile.setOnClickListener(v -> goToEditProfile());
            } else {
                Log.println(Log.WARN, "ProfileActivity argument exception", "What user do you want to display? Displaying null user for the moment");
                userIdToDisplay = NULL_USER;
            }
        }

        pvm.fetchProfile(userIdToDisplay);
        pvm.getProfileLiveData().observe(this, profile -> {
            setTextViewsTo(profile);
        });
    }

    private void setTextViewsTo(Profile user) {
        TextView name = findViewById(R.id.profileName);
        TextView email = findViewById(R.id.profileEmail);
        TextView bio = findViewById(R.id.profileBio);
        TextView gender = findViewById(R.id.profileGender);

        name.setText(user.getName());
        email.setText(user.getEmail());
        bio.setText(user.getBio());
        gender.setText(user.getGender().name());
    }

    private void goToEditProfile() {
        Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
        startActivity(intent);
        finish();
    }
}