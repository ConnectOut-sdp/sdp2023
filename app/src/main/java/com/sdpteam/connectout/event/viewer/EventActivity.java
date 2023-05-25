package com.sdpteam.connectout.event.viewer;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.sdpteam.connectout.profile.Profile.NULL_USER;

import java.util.Calendar;
import java.util.Locale;
import java.util.TimeZone;

import com.sdpteam.connectout.QrCode.QRcodeModalActivity;
import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.AuthenticatedUser;
import com.sdpteam.connectout.authentication.GoogleAuth;
import com.sdpteam.connectout.chat.ChatActivity;
import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.Event.EventRestrictions.RestrictionStatus;
import com.sdpteam.connectout.event.EventFirebaseDataSource;
import com.sdpteam.connectout.event.creator.SetEventRestrictionsActivity;
import com.sdpteam.connectout.post.view.PostCreatorActivity;
import com.sdpteam.connectout.post.view.PostsFragment;
import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileFirebaseDataSource;
import com.sdpteam.connectout.profile.ProfileViewModel;
import com.sdpteam.connectout.profileList.EventParticipantsListActivity;
import com.sdpteam.connectout.utils.WithFragmentActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.widget.Toolbar;

public class EventActivity extends WithFragmentActivity {

    public final static String PASSED_ID_KEY = "eventId";
    public final static String JOIN_EVENT = "I join!";
    public final static String INTERESTED = "I'm interested!";
    public final static String NOT_INTERESTED = "No longer interested";
    public final static String LEAVE_EVENT = "Leave event";

    public final static String MAKE_POST = "Make post";
    private final ActivityResultLauncher<Intent> qrCodeLauncher = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(), result -> {
    });
    private EventViewModel eventViewModel;
    private ProfileViewModel profileViewModel; //for event registration
    private String eventId;
    private String currentUserId;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        initViewModel();
        initToolbar();
        initPostsFragment();
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
        eventId = getIntent().getStringExtra(PASSED_ID_KEY);
        AuthenticatedUser user = new GoogleAuth().loggedUser();
        currentUserId = user == null ? NULL_USER : user.uid;
        profileViewModel = new ProfileViewModel(new ProfileFirebaseDataSource());

        eventViewModel = new EventViewModel(new EventFirebaseDataSource());
        eventViewModel.getEvent(eventId);
    }

    /**
     * Initialize the event's main display.
     */
    private void initEventView() {
        TextView title = findViewById(R.id.event_title);
        TextView description = findViewById(R.id.event_description);
        Button joinBtn = findViewById(R.id.event_join_button);
        Button interestedBtn = findViewById(R.id.event_interested_button);
        Button restrictionsBtn = findViewById(R.id.event_restrictions_button);
        Button participantsBtn = findViewById(R.id.event_participants_button);
        ImageButton chatBtn = findViewById(R.id.event_chat_btn);
        ImageButton sharePersonalQrCodeButton = findViewById(R.id.buttonShareEventQrCode);
        Button makePostBtn = findViewById(R.id.event_make_post_button);

        eventViewModel.getEventLiveData().observe(this, event ->
                updateEventView(event, title, description, joinBtn, interestedBtn, restrictionsBtn, participantsBtn, chatBtn, sharePersonalQrCodeButton, makePostBtn)
        );
    }

    /**
     * Upon modification of the given event, changes its view and some btn behaviors.
     */
    @SuppressLint("SetTextI18n")
    private void updateEventView(Event event,
                                 TextView title,
                                 TextView description,
                                 Button joinBtn,
                                 Button interestedBtn,
                                 Button restrictionsBtn,
                                 Button participantsBtn,
                                 ImageButton chatBtn,
                                 ImageButton shareQrCodeBtn,
                                 Button makePostBtn) {
        title.setText("- " + event.getTitle());
        description.setText(event.getDescription());

        joinBtn.setText(event.hasJoined(currentUserId) ? LEAVE_EVENT : JOIN_EVENT);
        joinBtn.setVisibility(event.getOrganizer().equals(currentUserId) ? GONE : VISIBLE);

        interestedBtn.setText(event.isInterested(currentUserId) ? NOT_INTERESTED : INTERESTED);
        interestedBtn.setVisibility(event.getOrganizer().equals(currentUserId) || event.hasJoined(currentUserId) ? GONE : VISIBLE);

        restrictionsBtn.setVisibility(event.getOrganizer().equals(currentUserId) ? VISIBLE : GONE);
        restrictionsBtn.setOnClickListener(v -> openRestrictions(event.getId()));

        chatBtn.setVisibility(event.hasJoined(currentUserId) || event.isInterested(currentUserId) ? VISIBLE : GONE);
        chatBtn.setOnClickListener(v -> openChat(event.getId()));

        shareQrCodeBtn.setOnClickListener(v -> {
            String qrCodeData = "event/" + event.getId();
            Intent intent = new Intent(EventActivity.this, QRcodeModalActivity.class);
            intent.putExtra("title", "Event QR code");
            intent.putExtra("qrCodeData", qrCodeData);
            qrCodeLauncher.launch(intent);
        });

        updateParticipantsButton(event, participantsBtn);
        participantsBtn.setOnClickListener(v -> showParticipants(event.getId()));

        joinBtn.setOnClickListener(v -> {
            if (event.hasJoined(currentUserId)) {
                eventViewModel.leaveEvent(currentUserId);
                return;
            }
            profileViewModel.fetchProfile(currentUserId);
            profileViewModel.getProfileLiveData().observeForever(p -> {
                final RestrictionStatus status = isRegistrationPossible(p, event);
                if (status != RestrictionStatus.ALL_RESTRICTIONS_SATISFIED) {
                    impossibleRegistrationToast(status.getMessage());
                    return;
                }
                eventViewModel.joinEvent(currentUserId, false);
            });
        });
        interestedBtn.setOnClickListener(v -> {
            if (event.isInterested(currentUserId)) {
                eventViewModel.leaveEvent(currentUserId); // remove as interested
            } else {
                eventViewModel.joinEvent(currentUserId, true);
            }
        });

        makePostBtn.setText(MAKE_POST);
        makePostBtn.setVisibility(event.hasJoined(currentUserId) || event.getOrganizer().equals(currentUserId) ? VISIBLE : GONE);
        makePostBtn.setOnClickListener(v -> PostCreatorActivity.openPostCreator(this, currentUserId, eventId, event.getTitle()));
    }

    private void openRestrictions(String eventId) {
        final Intent intent = new Intent(this, SetEventRestrictionsActivity.class);
        intent.putExtra(PASSED_ID_KEY, eventId);
        startActivity(intent);
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

    private void initPostsFragment() {
        PostsFragment postsFragment = new PostsFragment(eventId, null, false, false);
        replaceFragment(postsFragment, R.id.event_post_fragment_container);
    }

    private void showParticipants(String eventId) {
        final Intent intent = new Intent(this, EventParticipantsListActivity.class);
        intent.putExtra(PASSED_ID_KEY, eventId);
        startActivity(intent);
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
     * Before joining an event, the profile must meet the registration criteria
     */
    public RestrictionStatus isRegistrationPossible(Profile p, Event e) {
        if (p == null) {
            return RestrictionStatus.ALL_RESTRICTIONS_SATISFIED;
        } // for the null user
        if (p.getRating() < e.getRestrictions().getMinRating()) {
            return RestrictionStatus.INSUFFICIENT_RATING;
        }
        if (e.getParticipants().size() >= e.getRestrictions().getMaxNumParticipants()) {
            return RestrictionStatus.MAX_NUM_PARTICIPANTS_REACHED;
        }
        if (e.getRestrictions().getJoiningDeadline() < Calendar.getInstance(TimeZone.getTimeZone("GMT+1:00")).getTimeInMillis()) {
            return RestrictionStatus.JOINING_DEADLINE_PASSED;
        }
        return RestrictionStatus.ALL_RESTRICTIONS_SATISFIED;
    }

    /**
     * If the registration isn't possible, the user is informed of this issue through a toast
     */
    private void impossibleRegistrationToast(String message) {
        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_SHORT).show();
    }

    @Override
    protected void onResume() {
        super.onResume();
        eventViewModel.refreshEvent();
        initPostsFragment();
    }
}

