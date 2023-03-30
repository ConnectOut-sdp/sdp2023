package com.sdpteam.connectout.profileList;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.drawer.FilterFragment;
import com.sdpteam.connectout.utils.WithFragmentActivity;

import android.os.Bundle;
import android.widget.ToggleButton;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

/**
 * Toggles between list fragment and filter fragment. Allowing both a simplistic view of the list of profiles and a filtered one.
 */
public class ProfileListActivity extends WithFragmentActivity {
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            //Create the view of the activity.
            super.onCreate(savedInstanceState);
            setContentView(R.layout.activity_user_list);

            //Setup go back icon.
            Toolbar toolbar = findViewById(R.id.user_list_toolbar);
            setSupportActionBar(toolbar);
            toolbar.setNavigationOnClickListener(v -> this.finish());

            //Setup filter button activation.
            ToggleButton filterButton = findViewById(R.id.user_list_button);

            //Setup the different list view fragment (one with filter one without).
            ProfileListFragment listFragment = new ProfileListFragment();
            replaceFragment(listFragment, R.id.container_users_listview);
            ProfileFilterFragment filterFragment = new ProfileFilterFragment();

            filterButton.setOnClickListener(v -> {
                if (filterButton.isChecked()) {
                    //Stop all updates on the current list that lacks filtering.
                    listFragment.stopObservation();
                    replaceFragment(filterFragment, R.id.container_users_listview);
                } else {
                    //Stop all updates on the current list that possess filtering.
                    filterFragment.stopObservation();
                    replaceFragment(listFragment, R.id.container_users_listview);
                }
            });
        }



}