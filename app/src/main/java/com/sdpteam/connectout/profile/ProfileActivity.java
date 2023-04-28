package com.sdpteam.connectout.profile;

import static android.view.View.INVISIBLE;
import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.Authentication;
import com.sdpteam.connectout.authentication.GoogleAuth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sdpteam.connectout.authentication.AuthenticatedUser;
import com.squareup.picasso.Picasso;

/**
 * Once the Profile Activity has been created, this activity can be deleted and the intent in
 * EditProfileActivity must be adapted
 */
public class ProfileActivity extends AppCompatActivity {
    public final static String PASSED_ID_KEY = "uid";

    public static final String PROFILE_UID = "uid";

    private final ProfileViewModel pvm = new ProfileViewModel(new ProfileFirebaseDataSource());
    Authentication auth = new GoogleAuth();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

        Button editProfile = findViewById(R.id.buttonEditProfile);
        Button rateProfile = findViewById(R.id.buttonRateProfile);

        String userIdToDisplay = getIntent().getStringExtra(PROFILE_UID);
        AuthenticatedUser au = new GoogleAuth().loggedUser();
        String uid = (au == null) ? NULL_USER : au.uid;

        if (userIdToDisplay == null) {
            rateProfile.setVisibility(INVISIBLE);
            editProfile.setOnClickListener(v -> goToEditProfile());
            userIdToDisplay = uid;
        } else {
            editProfile.setVisibility(INVISIBLE);
            String finalUserIdToDisplay = userIdToDisplay;
            rateProfile.setOnClickListener(v -> goToProfileRate(finalUserIdToDisplay));
        }

        pvm.fetchProfile(userIdToDisplay);
        pvm.getProfileLiveData().observe(this, this::displayProfile);
    }

    private void displayProfile(Profile profile) {
        TextView name = findViewById(R.id.profileName);
        TextView email = findViewById(R.id.profileEmail);
        TextView bio = findViewById(R.id.profileBio);
        TextView gender = findViewById(R.id.profileGender);

        name.setText(profile.getName());
        email.setText(profile.getEmail());
        bio.setText(profile.getBio());
        gender.setText(profile.getGender().name());

        ImageView profilePicture = findViewById(R.id.profilePicture);
        String imageUrl = profile.getProfileImageUrl();
        Picasso.get().load(imageUrl).into(profilePicture);
    }

    private void goToEditProfile() {
        Intent intent = new Intent(ProfileActivity.this, EditProfileActivity.class);
        startActivity(intent);
        finish();
    }

    private void goToProfileRate(String id) {
        Intent intent = new Intent(ProfileActivity.this, ProfileRateActivity.class);
        intent.putExtra("uid", id);

        TextView viewById = findViewById(R.id.profileName);
        String userName = (String) viewById.getText();
        intent.putExtra("name", userName);
        startActivity(intent);
    }

    /**
     * Helper method to launch a profile activity from the source context
     * (made it to avoid code duplication)
     *
     * @param fromContext from where we are starting the intent
     * @param profileId   user id to open
     */
    public static void openProfile(Context fromContext, String profileId) {
        Intent intent = new Intent(fromContext, ProfileActivity.class);
        intent.putExtra(PASSED_ID_KEY, profileId);
        fromContext.startActivity(intent);
    }
}