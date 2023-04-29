package com.sdpteam.connectout.event.viewer;

import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.widget.Toolbar;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.AuthenticatedUser;
import com.sdpteam.connectout.authentication.GoogleAuth;
import com.sdpteam.connectout.chat.ChatActivity;
import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.EventFirebaseDataSource;
import com.sdpteam.connectout.event.creator.SetEventRestrictionsActivity;
import com.sdpteam.connectout.event.nearbyEvents.map.GPSCoordinates;
import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileFirebaseDataSource;
import com.sdpteam.connectout.profile.ProfileViewModel;
import com.sdpteam.connectout.utils.WithFragmentActivity;

import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import java.util.TimeZone;

public class EventActivity extends WithFragmentActivity {

    public final static String PASSED_ID_KEY = "eventId";
    public final static String JOIN_EVENT = "Join Event";
    public final static String LEAVE_EVENT = "Leave Event";
    public final static String ADD_RESTRICTIONS = "Change Restrictions";

    private EventViewModel eventViewModel;

    private ProfileViewModel profileViewModel; //for event registration
    private String currentUserId;

    private Profile currentProfile;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        initActivityArguments();
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
     * Setup the view models and the activity arguments
     */
    private void initActivityArguments() {
        String eventId = getIntent().getStringExtra(PASSED_ID_KEY);
        AuthenticatedUser user = new GoogleAuth().loggedUser();
        currentUserId = user == null ? NULL_USER : user.uid;
        profileViewModel = new ProfileViewModel(new ProfileFirebaseDataSource());

        //profileViewModel.fetchProfile(currentUserId);

        eventViewModel = new EventViewModel(new EventFirebaseDataSource());
        eventViewModel.getEvent(eventId);

        //currentProfile = profileViewModel.getProfileLiveData().getValue(); //TODO check that it doesn't introduce too much delay
    }

    /**
     * Initialize the event's main display.
     */
    private void initEventView() {
        TextView title = findViewById(R.id.event_title);
        TextView description = findViewById(R.id.event_description);
        Button participationBtn = findViewById(R.id.event_join_button);
        Button participantsBtn = findViewById(R.id.event_participants_button);
        ImageButton chatBtn = findViewById(R.id.event_chat_btn);

        participationBtn.setOnClickListener(v -> {
            eventViewModel.toggleParticipation(currentUserId, profileViewModel,
                    x -> impossibleRegistrationToast(x), (p,e) -> isRegistrationPossible(p,e),
                    e -> {final Intent intent = new Intent(this, SetEventRestrictionsActivity.class);
                        intent.putExtra(PASSED_ID_KEY, e.getId());
                        startActivity(intent);
            });
        });
        eventViewModel.getEventLiveData().observe(this, event ->{
            updateEventView(event, title, description, participationBtn, participantsBtn, chatBtn);
        });
        participantsBtn.setOnClickListener(v -> showParticipants(null));
    }

    /**
     * Upon modification of the given event, changes its view.
     */
    @SuppressLint("SetTextI18n")
    private void updateEventView(Event event, TextView title, TextView description, Button participationBtn, Button participantsBtn, ImageButton chatBtn) {
        title.setText("- " + event.getTitle());
        description.setText(event.getDescription());
        participationBtn.setText((event.getOrganizer().equals(currentUserId))? ADD_RESTRICTIONS : event.getParticipants().contains(currentUserId) ? LEAVE_EVENT : JOIN_EVENT);
        updateParticipantsButton(event, participantsBtn);
        chatBtn.setVisibility(event.getParticipants().contains(currentUserId) ? View.VISIBLE : View.INVISIBLE);
        chatBtn.setOnClickListener(v -> openChat(event.getId()));
        if (!event.getParticipants().contains(currentUserId)){ //TODO move this if-else to EventViewModel, toggleParticipation function
            profileViewModel.registerToEvent(new Profile.CalendarEvent(event.getId(), event.getTitle(), event.getDate()), currentUserId);
        }
        else{
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
     * @param event (Event): current displayed event.
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
     * @param eventId   event Id to open with.
     */
    public static void openEvent(Context fromContext, String eventId) {
        Intent intent = new Intent(fromContext, EventActivity.class);
        intent.putExtra(PASSED_ID_KEY, eventId);
        fromContext.startActivity(intent);
    }

    /**
     * Before joining an event, the profile must meet the registration criteria
     * */
    public Event.EventRestrictions.RestrictionStatus isRegistrationPossible(Profile p, Event e){
        if (e.getRestrictions().getJoiningDeadline() < Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00")).getTimeInMillis()){
            return Event.EventRestrictions.RestrictionStatus.JOINING_DEADLINE_PASSED;
        }
        if (p.getRating() < e.getRestrictions().getMinRating()){
            return Event.EventRestrictions.RestrictionStatus.INSUFFICIENT_RATING;
        }
        if (e.getParticipants().size() >= e.getRestrictions().getMaxNumParticipants()){
            return Event.EventRestrictions.RestrictionStatus.MAX_NUM_PARTICIPANTS_REACHED;
        }
        return Event.EventRestrictions.RestrictionStatus.ALL_RESTRICTIONS_SATISFIED;
    }

    /**
     * If the registration isn't possible, the user is informed of this issue through a toast
     * */
    private void impossibleRegistrationToast(String message){
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

}

