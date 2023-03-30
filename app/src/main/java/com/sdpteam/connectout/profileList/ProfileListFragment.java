package com.sdpteam.connectout.profileList;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileActivity;
import com.sdpteam.connectout.profile.ProfileFirebaseDataSource;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * Simple and non-filtered view of the list of profile.
 */
public class ProfileListFragment extends Fragment {

    private ListView listView;
    private ViewGroup container;
    private ProfilesAdapter profilesAdapter;
    private ProfileListViewModel viewModel;
    private Observer<List<Profile>> profilesObserver;
    private LiveData<List<Profile>> observed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the view for this fragment in its container.
        View contentView = inflater.inflate(R.layout.fragment_user_list, container, false);
        this.container = container;

        profilesObserver = this::showProfileList;
        viewModel = new ProfileListViewModel(new ProfileFirebaseDataSource());
        observed = viewModel.getUserListLiveData();
        changeObserved(ProfileFirebaseDataSource.ProfileOrderingOption.NONE, null);

        //Setup the profile Adapter, which will create the view for each given profile & give it to the view.
        profilesAdapter = new ProfilesAdapter(container.getContext(), R.layout.adapter_text_view, new ArrayList<>());
        listView = contentView.findViewById(R.id.user_list_view);
        listView.setAdapter(profilesAdapter);

        //On an item being clicked in the list, launch its profile view.
        listView.setOnItemClickListener((parent, view, position, id) -> {
            Intent profileIntent = new Intent(getContext(), ProfileActivity.class);
            profileIntent.putExtra(ProfileActivity.PASSED_ID_KEY, profilesAdapter.getItem(position).getId());
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
        //Filter out all inadequate profiles.
        profiles = profiles.stream().filter(p -> !Objects.equals(p.getName(), "")).collect(Collectors.toList());

        profilesAdapter = new ProfilesAdapter(container.getContext(), R.layout.adapter_text_view, profiles);
        listView.setAdapter(profilesAdapter);
    }

    /**
     * Update what type of filtered list is being observed on the screen.
     *
     * @param option    (ProfileOrderingOption): different possible filtering options of the list.
     * @param userInput (String): user inputs that indicates filters values.
     */
    public void changeObserved(ProfileFirebaseDataSource.ProfileOrderingOption option, String userInput) {
        if (viewModel == null) {
            return;
        }
        if (observed != null) {
            observed.removeObserver(profilesObserver);
        }
        viewModel.getListOfProfile(option, userInput);
        //Observe the filtered list.
        observed.observe(getViewLifecycleOwner(), profilesObserver);

    }

    /**
     * Remove all observations on the current filtered list.
     */
    public void stopObservation() {
        if (observed != null) {
            observed.removeObserver(profilesObserver);
        }
    }
}