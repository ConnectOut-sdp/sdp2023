package com.sdpteam.connectout.profileList;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;

import com.sdpteam.connectout.R;

import java.util.ArrayList;

public class ProfileListFragment extends Fragment {

    private final ProfilesViewModel viewModel;

    public ProfileListFragment(ProfilesViewModel viewModel) {
        this.viewModel = viewModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View contentView = inflater.inflate(R.layout.fragment_user_list, container, false);

        final ProfilesAdapter adapter = new ProfilesAdapter(container.getContext(), R.layout.adapter_text_view, new ArrayList<>());
        final ListView listView = contentView.findViewById(R.id.user_list_view);
        listView.setAdapter(adapter);

        viewModel.refreshProfiles();
        viewModel.getProfilesLiveData().observeForever(profiles -> {
            adapter.clear();
            adapter.addAll(profiles);
            adapter.notifyDataSetChanged();
        });

        return contentView;
    }

}