package com.sdpteam.connectout.profileList;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.drawer.DrawerFragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;

public class CommunityFragment extends DrawerFragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_community, container, false);

        final Toolbar toolbar = view.findViewById(R.id.user_list_toolbar);
        final Button filterBtn = view.findViewById(R.id.user_list_button);
        setupToolBar(filterBtn, toolbar, "Filter", null); // to hide/remove button

        final FilteredProfileListFragment profilesFragment = new FilteredProfileListFragment();
        getChildFragmentManager().beginTransaction().replace(R.id.container_users_listview, profilesFragment).commit();

        return view;
    }
}
