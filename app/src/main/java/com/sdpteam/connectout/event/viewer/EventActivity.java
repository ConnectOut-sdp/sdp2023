package com.sdpteam.connectout.event.viewer;

import static android.view.View.INVISIBLE;
import static android.view.View.VISIBLE;
import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.widget.Toolbar;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.AuthenticatedUser;
import com.sdpteam.connectout.authentication.GoogleAuth;
import com.sdpteam.connectout.chat.ChatActivity;
import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.EventFirebaseDataSource;
import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileFirebaseDataSource;
import com.sdpteam.connectout.profile.ProfileViewModel;
import com.sdpteam.connectout.utils.WithFragmentActivity;

import java.util.List;
import java.util.Locale;

public class EventActivity extends WithFragmentActivity {

    public final static String PASSED_ID_KEY = "eventId";
    public final static String JOIN_EVENT = "I join!";
    public final static String INTERESTED = "I'm interested!";
    public final static String NOT_INTERESTED = "No longer interested";
    public final static String LEAVE_EVENT = "Leave event";

    private EventViewModel eventViewModel;

    private ProfileViewModel profileViewModel; //for event registration
    private String currentUserId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        initViewModel();
        initToolbar();
        initMapFragment();
        initEventView();
    }

    /**
     * Setup the tool bar, for returning upon completion of view.
     */
    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.event_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> this.finish());
    }

    /**
     * Setup the view model.
     */
    private void initViewModel() {
        String eventId = getIntent().getStringExtra(PASSED_ID_KEY);
        eventViewModel = new EventViewModel(new EventFirebaseDataSource());
        eventViewModel.getEvent(eventId);
        profileViewModel = new ProfileViewModel(new ProfileFirebaseDataSource());

        AuthenticatedUser user = new GoogleAuth().loggedUser();
        currentUserId = user == null ? NULL_USER : user.uid;
    }

    /**
     * Initialize the event's main display.
     */
    private void initEventView() {
        TextView title = findViewById(R.id.event_title);
        TextView description = findViewById(R.id.event_description);
        Button joinBtn = findViewById(R.id.event_join_button);
        Button interestedBtn = findViewById(R.id.event_interested_button);
        Button participantsBtn = findViewById(R.id.event_participants_button);
        ImageButton chatBtn = findViewById(R.id.event_chat_btn);

        eventViewModel.getEventLiveData().observe(this, event ->
                updateEventView(event, title, description, joinBtn, interestedBtn, participantsBtn, chatBtn)
        );
        participantsBtn.setOnClickListener(v -> showParticipants(null));
    }

    /**
     * Upon modification of the given event, changes its view and some btn behaviors.
     */
    @SuppressLint("SetTextI18n")
    private void updateEventView(Event event, TextView title, TextView description, Button joinBtn, Button interestedBtn, Button participantsBtn, ImageButton chatBtn) {
        title.setText("- " + event.getTitle());
        description.setText(event.getDescription());

        joinBtn.setText(event.hasJoined(currentUserId) ? LEAVE_EVENT : JOIN_EVENT);
        interestedBtn.setText(event.isInterested(currentUserId) ? NOT_INTERESTED : INTERESTED);
        interestedBtn.setVisibility(event.hasJoined(currentUserId) ? INVISIBLE : VISIBLE);
        chatBtn.setVisibility(event.hasJoined(currentUserId) || event.isInterested(currentUserId) ? VISIBLE : INVISIBLE);
        chatBtn.setOnClickListener(v -> openChat(event.getId()));
        updateParticipantsButton(event, participantsBtn);

        joinBtn.setOnClickListener(v -> {
            if (event.hasJoined(currentUserId)) {
                eventViewModel.leaveEvent(currentUserId);
            } else {
                eventViewModel.joinEvent(currentUserId, false);
            }
        });
        interestedBtn.setOnClickListener(v -> {
            if (event.isInterested(currentUserId)) {
                eventViewModel.leaveEvent(currentUserId); // remove as interested
            } else {
                eventViewModel.joinEvent(currentUserId, true);
            }
        });

        if (!event.hasJoined(currentUserId)) {
            profileViewModel.registerToEvent(new Profile.CalendarEvent(event.getId(), event.getTitle(), event.getDate()), currentUserId);
        } else {
            //TODO unregister from event (need to create function in profileDataSource)
        }
    }

    private void openChat(String eventID) {
        final Intent intent = new Intent(this, ChatActivity.class);
        intent.putExtra("chatId", eventID);
        startActivity(intent);
    }

    /**
     * Initializes the map of the event.
     */
    private void initMapFragment() {
        EventMapViewFragment map = new EventMapViewFragment(eventViewModel);
        replaceFragment(map, R.id.event_fragment_container);
    }

    private void showParticipants(List<String> participants) {
        // TODO launch new activity (or pop-up) with list of profiles
    }

    /**
     * Updates the participant button's text to display the event's number of participants.
     *
     * @param event           (Event): current displayed event.
     * @param participantsBtn (Button): participant button of the view.
     */
    private void updateParticipantsButton(Event event, Button participantsBtn) {
        String participantsBtnText = String.format(Locale.getDefault(),
                getString(R.string.participants_size_format),
                getString(R.string.participants),
                event.getParticipants().size());
        participantsBtn.setText(participantsBtnText);
    }

    /**
     * Helper method to launch a event activity from the source context
     * (made it to avoid code duplication)
     *
     * @param fromContext from where we are starting the intent
     * @param eventId     event Id to open with.
     */
    public static void openEvent(Context fromContext, String eventId) {
        Intent intent = new Intent(fromContext, EventActivity.class);
        intent.putExtra(PASSED_ID_KEY, eventId);
        fromContext.startActivity(intent);
    }

}

