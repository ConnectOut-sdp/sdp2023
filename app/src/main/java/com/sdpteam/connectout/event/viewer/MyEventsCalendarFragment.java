package com.sdpteam.connectout.event.viewer;

import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;

import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.firebase.ui.database.FirebaseListOptions;
import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.GoogleAuth;
import com.sdpteam.connectout.drawer.DrawerFragment;
import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileFirebaseDataSource;
import com.sdpteam.connectout.profile.ProfileViewModel;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * This fragment sets up the Calendar UserInterface
 * It consists in the list of events to which a user has registered
 */
public class MyEventsCalendarFragment extends DrawerFragment {
    private final String profileId;
    public ProfileViewModel viewModel = new ProfileViewModel(new ProfileFirebaseDataSource());

    public MyEventsCalendarFragment() {
        GoogleAuth googleAuth = new GoogleAuth();
        this.profileId = googleAuth.isLoggedIn() ? googleAuth.loggedUser().uid : NULL_USER;
    }

    public MyEventsCalendarFragment(String profileId) {
        this.profileId = profileId;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_registered_events_calendar, container, false);

        //set up the ListView
        setUpListAdapter(view);

        ListView listOfRegisteredEvents = view.findViewById(R.id.list_of_registered_events);

        //on click, open event
        listOfRegisteredEvents.setOnItemClickListener((parent, v, position, id) -> {
            TextView item = v.findViewById(R.id.registered_event_id);
            EventActivity.openEvent(getContext(), item.getText().toString());
        });

        return view;
    }

    /**
     * The view must not interact directly with Firebase, as such the FirebaseListAdapter is created
     * by the model.
     * However, the adapter needs indirect access to the view elements as such we pass lambdas so that
     * the Model has no direct access.
     */
    private void setUpListAdapter(View view) {
        ListView listOfRegisteredEvents = view.findViewById(R.id.list_of_registered_events);
        listOfRegisteredEvents.setStackFromBottom(true);

        Function<FirebaseListOptions.Builder<Profile.CalendarEvent>, FirebaseListOptions.Builder<Profile.CalendarEvent>> setLayout = a -> a.setLayout(R.layout.registered_event);
        Function<FirebaseListOptions.Builder<Profile.CalendarEvent>, FirebaseListOptions.Builder<Profile.CalendarEvent>> setLifecycleOwner = a -> a.setLifecycleOwner(getViewLifecycleOwner());
        BiConsumer<View, Profile.CalendarEvent> populateView = (v, calendarEvent) -> {
            TextView registeredEventTitle = v.findViewById(R.id.registered_event_title);
            TextView registeredEventTime = v.findViewById(R.id.registered_event_time);
            TextView registeredEventId = v.findViewById(R.id.registered_event_id);

            registeredEventTitle.setText(calendarEvent.getEventTitle());
            registeredEventId.setText(calendarEvent.getEventId());
            // Format the date before showing it
            registeredEventTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                    calendarEvent.getEventDate()));
            registeredEventTime.setGravity(Gravity.CENTER);
            registeredEventTitle.setGravity(Gravity.CENTER);
        };
        Consumer<ListAdapter> setAdapter = listOfRegisteredEvents::setAdapter;
        viewModel.setUpListAdapter(setLayout, setLifecycleOwner, populateView, setAdapter, profileId);
    }
}