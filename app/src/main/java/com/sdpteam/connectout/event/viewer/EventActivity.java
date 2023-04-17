package com.sdpteam.connectout.event.viewer;

import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;
import static java.lang.String.format;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.Observer;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.AuthenticatedUser;
import com.sdpteam.connectout.authentication.GoogleAuth;
import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.EventFirebaseDataSource;
import com.sdpteam.connectout.utils.SingleShotObserver;
import com.sdpteam.connectout.utils.WithFragmentActivity;

import java.util.List;
import java.util.Locale;

public class EventActivity extends WithFragmentActivity {

    public final static String PASSED_ID_KEY = "eventId";
    private final static String JOIN_EVENT = "Join Event";
    private final static String LEAVE_EVENT = "Leave Event";

    private EventViewModel viewModel;
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        Toolbar toolbar = findViewById(R.id.event_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> this.finish());

        final AuthenticatedUser user = new GoogleAuth().loggedUser();
        currentUserId = user == null ? NULL_USER : user.uid;

        final String eventId = getIntent().getStringExtra(PASSED_ID_KEY);
        viewModel = new EventViewModel(new EventFirebaseDataSource());
        viewModel.getEvent(eventId);

        final TextView title = findViewById(R.id.event_title);
        final TextView description = findViewById(R.id.event_description);
        final Button participationBtn = findViewById(R.id.event_join_button);
        final Button participantsBtn = findViewById(R.id.event_participants_button);

        Observer<Event> initializationObserver = e -> initializeEventView(e, title, description, participationBtn, participantsBtn);
        viewModel.getEventLiveData().observe(this, new SingleShotObserver<>(initializationObserver));

        participationBtn.setOnClickListener(v -> swapEventParticipation(participationBtn));
        participantsBtn.setOnClickListener(v -> viewModel.getEventLiveData().observe(this, e -> showParticipants(e.getParticipants())));
    }

    /**
     * Initializes the view of the activity upon the given event.
     *
     * @param event            (Event): current selected event.
     * @param title            (TextView): title of the selected event
     * @param description      (TextView): description of the selected event
     * @param participationBtn (Button): button that enables participating to the event
     * @param participantsBtn  (Button): button that displays participants of the event
     */
    @SuppressLint("SetTextI18n")
    private void initializeEventView(Event event, TextView title, TextView description, Button participationBtn, Button participantsBtn) {
        title.setText("- "+event.getTitle());
        description.setText(event.getDescription());
        String participantsBtnText = format(Locale.getDefault(), "%s (%d)", getString(R.string.participants), event.getParticipants().size());

        participantsBtn.setText(participantsBtnText);
        participationBtn.setText(event.getParticipants().contains(currentUserId) ? LEAVE_EVENT : JOIN_EVENT);

        initMapFragment();
    }

    /**
     * Initializes the map view of the activity using the given event.
     */
    private void initMapFragment() {
        final EventMapViewFragment map = new EventMapViewFragment(viewModel);
        replaceFragment(map, R.id.event_fragment_container);
    }

    /**
     * @param participants (List<String>): list of participating persons.
     */
    private void showParticipants(List<String> participants) {
        // TODO launch new activity (or pop-up) with list of profiles
    }

    /**
     * Updates participation status of the user in the event based on their current status.
     *
     * @param participationBtn (Button): button which says if you either want to leave or join an event.
     */
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

    /**
     * Helper method to launch an event activity from the source context
     *
     * @param fromContext from where we are starting the intent
     * @param eventId     event to display
     */
    public static void openEvent(Context fromContext, String eventId) {
        Intent intent = new Intent(fromContext, EventActivity.class);
        intent.putExtra(PASSED_ID_KEY, eventId);
        fromContext.startActivity(intent);
    }

}
