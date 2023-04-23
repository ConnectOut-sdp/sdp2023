package com.sdpteam.connectout.profileList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.drawer.DrawerActivity;
import com.sdpteam.connectout.event.nearbyEvents.filter.EventsFilterDialog;
import com.sdpteam.connectout.utils.DrawerFragment;
import com.sdpteam.connectout.utils.WithFragmentActivity;

public class ProfilesContainerFragment extends DrawerFragment {


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_user_list, container, false);

        Toolbar toolbar = view.findViewById(R.id.user_list_toolbar);
        ToggleButton filterButton = view.findViewById(R.id.user_list_button);

        ProfileListFragment listFragment = new ProfileListFragment();
        ProfileFilterFragment filterFragment = new ProfileFilterFragment();

        View.OnClickListener listener = v -> {
            if (filterButton.isChecked()) {
                // Stop all updates on the current list that lacks filtering.
                listFragment.stopObservation();
                getChildFragmentManager().beginTransaction().replace(R.id.container_users_listview, filterFragment).commit();
            } else {
                // Stop all updates on the current list that possess filtering.
                filterFragment.stopObservation();
                getChildFragmentManager().beginTransaction().replace(R.id.container_users_listview, listFragment).commit();
            }
        };

        setupToolBar(filterButton,toolbar,"Filter",listener);

        getChildFragmentManager().beginTransaction().replace(R.id.container_users_listview, filterFragment).commit();

        return view;
    }
}
