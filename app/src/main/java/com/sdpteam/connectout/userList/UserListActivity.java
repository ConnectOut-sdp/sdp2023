package com.sdpteam.connectout.userList;

import android.os.Bundle;
import android.widget.ToggleButton;

import androidx.appcompat.widget.Toolbar;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.WithFragmentActivity;
import com.sdpteam.connectout.drawer.FilterFragment;

public class UserListActivity extends WithFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        Toolbar toolbar = findViewById(R.id.user_list_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> this.finish());

        ToggleButton filterButton = findViewById(R.id.user_list_button);

        UserListFragment listFragment = new UserListFragment();
        replaceFragment(listFragment, R.id.container_users_listview);

        FilterFragment filterFragment  = new FilterFragment();

        filterButton.setOnClickListener(v -> {
            if (filterButton.isChecked()) {
                listFragment.stopObservation();
                replaceFragment(filterFragment, R.id.container_users_listview);
            } else {
                filterFragment.stopObservation();
                replaceFragment(listFragment, R.id.container_users_listview);
            }
        });
    }


}