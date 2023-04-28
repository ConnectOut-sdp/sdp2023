package com.sdpteam.connectout.profileList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.utils.DrawerFragment;

public class ProfilesContainerFragment extends DrawerFragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_list, container, false);

        Toolbar toolbar = view.findViewById(R.id.user_list_toolbar);
        Button filterButton = view.findViewById(R.id.user_list_button);

        ProfileListFragment listFragment = new ProfileListFragment();
        ProfileFilterFragment filterFragment = new ProfileFilterFragment();

        View.OnClickListener listener = new View.OnClickListener() {
            private boolean withoutFilter;

            @Override
            public void onClick(View v) {
                if (withoutFilter) {
                    // Stop all updates on the current list that lacks filtering.
                    listFragment.stopObservation();
                    getChildFragmentManager().beginTransaction().replace(R.id.container_users_listview, filterFragment).commit();
                    withoutFilter = false;
                } else {
                    // Stop all updates on the current list that possess filtering.
                    filterFragment.stopObservation();
                    getChildFragmentManager().beginTransaction().replace(R.id.container_users_listview, listFragment).commit();
                    withoutFilter = true;
                }
            }
        };

        setupToolBar(filterButton, toolbar, "Filter", listener);

        getChildFragmentManager().beginTransaction().replace(R.id.container_users_listview, filterFragment).commit();

        return view;
    }
}
