package com.sdpteam.connectout.event.viewer;

import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.GoogleAuth;
import com.sdpteam.connectout.drawer.DrawerFragment;
import com.sdpteam.connectout.event.EventFirebaseDataSource;
import com.sdpteam.connectout.event.nearbyEvents.EventsViewModel;
import com.sdpteam.connectout.event.nearbyEvents.filter.CalendarEventFilter;
import com.sdpteam.connectout.event.nearbyEvents.list.EventsCalendarAdapter;
import com.sdpteam.connectout.event.nearbyEvents.list.EventsListViewFragment;

/**
 * This fragment sets up the Calendar UserInterface
 * It consists in the list of events to which a user has registered
 */
public class MyEventsCalendarFragment extends DrawerFragment {
    private final String profileId;
    private EventsListViewFragment eventsListViewFragment;

    private final EventsViewModel eventsViewModel = new EventsViewModel(new EventFirebaseDataSource());

    public MyEventsCalendarFragment() {
        GoogleAuth googleAuth = new GoogleAuth();
        this.profileId = googleAuth.isLoggedIn() ? googleAuth.loggedUser().uid : NULL_USER;

        eventsViewModel.setFilter(new CalendarEventFilter(profileId));
    }

    public MyEventsCalendarFragment(String profileId) {
        this.profileId = profileId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registered_events_calendar, container, false);

        EventsViewModel viewModel = new EventsViewModel(new EventFirebaseDataSource());

        eventsListViewFragment = new EventsListViewFragment(viewModel, new EventsCalendarAdapter(getContext(), R.layout.registered_event));
        getChildFragmentManager().beginTransaction().replace(R.id.list_of_registered_events, eventsListViewFragment).commit();
        return view;
    }
}