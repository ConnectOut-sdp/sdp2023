package com.sdpteam.connectout.userList;

import android.os.Bundle;
import android.widget.ToggleButton;

import androidx.appcompat.widget.Toolbar;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.WithFragmentActivity;

public class UserListActivity extends WithFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Create the view of the activity.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        //Setup, go back icon.
        Toolbar toolbar = findViewById(R.id.user_list_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> this.finish());

        //Setup filter button activation.
        ToggleButton filterButton = findViewById(R.id.user_list_button);

        //Setup the different list view fragment (one with filter one without).
        UserListFragment listFragment = new UserListFragment();
        replaceFragment(listFragment, R.id.container_users_listview);

        FilterFragment filterFragment  = new FilterFragment();

        //Upon switching filter option
        filterButton.setOnClickListener(v -> {
            //If turning on the filter
            if (filterButton.isChecked()) {
                //Stop all updates on the current list that lacks filtering.
                listFragment.stopObservation();
                //Replace the view with one that can retrieve filters.
                replaceFragment(filterFragment, R.id.container_users_listview);
            } else {
                //Stop all updates on the current list that possess filtering.
                filterFragment.stopObservation();
                //Replace the view with default one.
                replaceFragment(listFragment, R.id.container_users_listview);
            }
        });
    }


}