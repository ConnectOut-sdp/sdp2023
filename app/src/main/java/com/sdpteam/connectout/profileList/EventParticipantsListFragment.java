package com.sdpteam.connectout.profileList;

import android.content.Intent;
import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import static com.sdpteam.connectout.event.viewer.EventActivity.PASSED_ID_KEY;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.event.EventFirebaseDataSource;
import com.sdpteam.connectout.event.viewer.EventViewModel;
import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileFirebaseDataSource;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

/**
 * non-filtered view of the list of profiles registered to an event
 */
public class EventParticipantsListFragment extends Fragment {
    private ListView listView;
    private ViewGroup container;
    private ProfilesAdapter profilesAdapter;
    private ProfileListViewModel viewModel;
    private Observer<List<Profile>> profilesObserver;
    private LiveData<List<Profile>> observed;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the view for this fragment in its container.
        String eventId = getArguments().getString(PASSED_ID_KEY);
        View contentView = inflater.inflate(R.layout.fragment_user_list, container, false);
        this.container = container;

        profilesObserver = this::showProfileList;
        viewModel = new ProfileListViewModel(new ProfileFirebaseDataSource());
        observed = viewModel.getUserListLiveData();

        changeObserved(ProfileFirebaseDataSource.ProfileOrderingOption.EVENT_PARTICIPANTS, eventId);

        //Setup the profile Adapter, which will create the view for each given profile & give it to the view.
        profilesAdapter = new ProfilesAdapter(container.getContext(), R.layout.adapter_text_view, new ArrayList<>());
        listView = contentView.findViewById(R.id.user_list_view);
        listView.setAdapter(profilesAdapter);

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
        if (profiles.isEmpty()) {
            Toast.makeText(getApplicationContext(), "No results, your filter might be too precise!", Toast.LENGTH_LONG).show();
        }
        profilesAdapter = new ProfilesAdapter(container.getContext(), R.layout.adapter_text_view, profiles);
        listView.setAdapter(profilesAdapter);
    }
    public void changeObserved(ProfileFirebaseDataSource.ProfileOrderingOption option, String userInput) {
        if (viewModel == null) {
            return;
        }
        if (!observed.hasActiveObservers()) {
            observed.observe(getViewLifecycleOwner(), profilesObserver);
        }
        viewModel.getListOfProfile(option, userInput);
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