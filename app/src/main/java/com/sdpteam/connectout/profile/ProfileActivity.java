package com.sdpteam.connectout.profile;

import static android.view.View.INVISIBLE;
import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;
import static com.sdpteam.connectout.profile.ProfileFragment.PASSED_ID_KEY;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.Authentication;
import com.sdpteam.connectout.authentication.GoogleAuth;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.AuthenticatedUser;
import com.sdpteam.connectout.authentication.Authentication;
import com.sdpteam.connectout.authentication.GoogleAuth;
import com.sdpteam.connectout.utils.WithFragmentActivity;

/**
 * Once the Profile Activity has been created, this activity can be deleted and the intent in
 * EditProfileActivity must be adapted
 */
public class ProfileActivity extends WithFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);

        String profileId = getIntent().getStringExtra(PASSED_ID_KEY);
        replaceFragment(ProfileFragment.setupFragment(profileId), R.id.fragment_container);
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