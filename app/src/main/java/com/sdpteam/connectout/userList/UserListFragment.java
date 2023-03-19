package com.sdpteam.connectout.userList;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.sdpteam.connectout.PassedField;
import com.sdpteam.connectout.R;
import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileActivity;
import com.sdpteam.connectout.profile.ProfileModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class UserListFragment extends Fragment {

    private ListView listView;
    private ViewGroup container;
    private ProfilesAdapter profilesAdapter;
    private final UserListViewModel viewModel;
    private final Observer<List<Profile>> profilesObserver;
    private LiveData<List<Profile>> observed;

    public UserListFragment() {
        profilesObserver = this::showProfileList;
        viewModel = new UserListViewModel(new ProfileModel());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the view for this fragment in its container.
        View contentView = inflater.inflate(R.layout.fragment_user_list, container, false);
        this.container = container;

        //Observe changes on the profile list.
        changeObserved(ProfileListOption.NONE, null);

        //Setup the profile Adapter, which will create the view for each given profile & give it to the view.
        profilesAdapter = new ProfilesAdapter(container.getContext(), R.layout.adapter_text_view, new ArrayList<>());
        listView = contentView.findViewById(R.id.user_list_view);
        listView.setAdapter(profilesAdapter);

        //On an item being clicked in the list, launch its profile view.
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent profileIntent = new Intent(getContext(), ProfileActivity.class);
            profileIntent.putExtra(PassedField.UserId.toString(), profilesAdapter.getItem(position).getId());
            startActivity(profileIntent);
        });
        return contentView;
    }

    /**
     * Update the list of profiles in the view.
     *
     * @param profiles (List<Profile>): new list of profiles to update.
     */
    public void showProfileList(List<Profile> profiles) {
        //Filter out all elements with null names
        profiles = profiles.stream().filter(p -> p.getName() != null).collect(Collectors.toList());

        //Setup the view with the new Adapter containing the updated list.
        profilesAdapter = new ProfilesAdapter(container.getContext(), R.layout.adapter_text_view, profiles);
        listView.setAdapter(profilesAdapter);
    }

    public void changeObserved(ProfileListOption option, List<String> arguments) {
        if (observed != null) {
            observed.removeObserver(profilesObserver);
        }

        observed = viewModel.getListOfProfile(option, arguments);
        observed.observe(getViewLifecycleOwner(), profilesObserver);

    }

    public void stopObservation() {
        if (observed != null) {
            observed.removeObserver(profilesObserver);
        }
    }


}