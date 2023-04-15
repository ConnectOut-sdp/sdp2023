package com.sdpteam.connectout.event.viewer;

import static java.lang.String.format;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.ViewModelProvider;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.AuthenticatedUser;
import com.sdpteam.connectout.authentication.GoogleAuth;
import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.EventFirebaseDataSource;
import com.sdpteam.connectout.event.EventRepository;
import com.sdpteam.connectout.event.nearbyEvents.EventsViewModel;
import com.sdpteam.connectout.event.nearbyEvents.EventsViewModelFactory;
import com.sdpteam.connectout.event.nearbyEvents.filter.EventFilter;
import com.sdpteam.connectout.event.nearbyEvents.filter.ProfilesFilter;
import com.sdpteam.connectout.event.nearbyEvents.map.EventsMapViewFragment;
import com.sdpteam.connectout.event.nearbyEvents.map.GPSCoordinates;
import com.sdpteam.connectout.utils.WithFragmentActivity;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;

public class EventActivity extends WithFragmentActivity {

    public final static String PASSED_ID_KEY = "eventId";
    private final static String JOIN_EVENT = "Join Event";
    private final static String LEAVE_EVENT = "Leave Event";

    private final EventViewModel viewModel = new EventViewModel(new EventFirebaseDataSource());
    private final String currentUserId = (new GoogleAuth().loggedUser()).uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        final String eventId = getIntent().getStringExtra(PASSED_ID_KEY);
        viewModel.getEvent(eventId);

        final TextView title = findViewById(R.id.event_title);
        final TextView description = findViewById(R.id.event_description);
        final Button participationBtn = findViewById(R.id.event_join_button);
        final Button participantsBtn = findViewById(R.id.event_participants_button);
        viewModel.getEventLiveData().observe(this, e -> initializeEventView( e, title,  description,  participationBtn,  participantsBtn));

        participationBtn.setOnClickListener(v -> swapEventParticipation(participationBtn));
        participantsBtn.setOnClickListener(v -> viewModel.getEventLiveData().observe(this, e -> showParticipants(e.getParticipants())));

        initMapFragment(Event.NULL_EVENT);
    }

    private void initMapFragment(Event event) {

        // Model that returns a singleton of the event as the event list
        //TODO connect to firebase.
        final EventRepository mapModel = new EventRepository() {
            @Override
            public boolean saveEvent(Event event) {
                return false;
            }

            @Override
            public CompletableFuture<Event> getEvent(String eventId) {
                return null;
            }

            @Override
            public CompletableFuture<Boolean> joinEvent(String eventId, String participantId) {
                return null;
            }

            @Override
            public CompletableFuture<Boolean> leaveEvent(String eventId, String participantId) {
                return null;
            }

            @Override
            public CompletableFuture<Event> getEvent(String userId, String title) {
                return null;
            }

            @Override
            public String getUniqueId() {
                return null;
            }

            @Override
            public CompletableFuture<List<Event>> getEventsByFilter(EventFilter eventFilter, ProfilesFilter profilesFilter) {
                return CompletableFuture.completedFuture(Collections.singletonList(event));
            }
        };

        // Implicitly instantiating EventsViewModel to use that instance back in MapViewFragment
        final EventsViewModel mapViewModel = new ViewModelProvider(this, new EventsViewModelFactory(mapModel)).get(EventsViewModel.class);

        final EventsMapViewFragment map = new EventsMapViewFragment(mapViewModel);
        replaceFragment(map, R.id.event_fragment_container);
    }

    private void showParticipants(List<String> participants) {
        // TODO launch new activity (or pop-up) with list of profiles
    }

    @SuppressLint("SetTextI18n")
    private void swapEventParticipation(Button participationBtn) {
        viewModel.getEventLiveData().observe(this, event -> {
            if (event.getParticipants().contains(currentUserId)) {
                viewModel.joinEvent();
                participationBtn.setText(LEAVE_EVENT);
            } else {
                viewModel.leaveEvent();
                participationBtn.setText(JOIN_EVENT);
            }
        });

    }

    private void initializeEventView(Event event,TextView title, TextView description, Button participationBtn, Button participantsBtn){
        title.setText(event.getTitle());
        description.setText(event.getDescription());
        String participantsBtnText = format(Locale.getDefault(), "%s (%d)", participantsBtn.getText(), event.getParticipants().size());
        participantsBtn.setText(participantsBtnText);
        participationBtn.setText(event.getParticipants().contains(currentUserId) ? LEAVE_EVENT : JOIN_EVENT);
    }
}
