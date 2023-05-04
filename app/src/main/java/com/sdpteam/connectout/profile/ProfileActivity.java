package com.sdpteam.connectout.profile;

import static com.sdpteam.connectout.profile.ProfileFragment.PASSED_ID_KEY;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.utils.WithFragmentActivity;

/**
 * Activity showing the current user's information
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