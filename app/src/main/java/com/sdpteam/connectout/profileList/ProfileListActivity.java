package com.sdpteam.connectout.profileList;

import android.os.Bundle;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.utils.WithFragmentActivity;

/**
 * Toggles between list fragment and filter fragment. Allowing both a simplistic view of the list of profiles and a filtered one.
 */
public class ProfileListActivity extends WithFragmentActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Create the view of the activity.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        replaceFragment(new ProfilesContainerFragment(), R.id.fragment_container);
    }
}