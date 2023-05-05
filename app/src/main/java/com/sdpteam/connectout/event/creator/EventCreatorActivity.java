package com.sdpteam.connectout.event.creator;

import static java.util.Collections.singletonList;

import java.util.ArrayList;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.AuthenticatedUser;
import com.sdpteam.connectout.authentication.GoogleAuth;
import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.EventFirebaseDataSource;
import com.sdpteam.connectout.event.nearbyEvents.map.GPSCoordinates;
import com.sdpteam.connectout.notifications.EventNotificationManager;
import com.sdpteam.connectout.profile.EditProfileActivity;
import com.sdpteam.connectout.utils.DateSelectors;
import com.sdpteam.connectout.utils.WithFragmentActivity;
import com.sdpteam.connectout.validation.EventCreationValidator;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.widget.Toolbar;

public class EventCreatorActivity extends WithFragmentActivity {
    private EventCreatorViewModel eventCreatorViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creator);

        if (eventCreatorViewModel == null) {
            eventCreatorViewModel = new EventCreatorViewModel(new EventFirebaseDataSource());
        }
        Toolbar toolbar = findViewById(R.id.event_creator_toolbar);
        setSupportActionBar(toolbar);
        LocationPicker mapFragment = new LocationPicker();
        replaceFragment(mapFragment, R.id.event_creator_fragment_container);
        toolbar.setNavigationOnClickListener(v -> this.finish());
        Button saveButton = findViewById(R.id.event_creator_save_button);

        EditText eventTitle = findViewById(R.id.event_creator_title);
        EditText eventDescription = findViewById(R.id.event_creator_description);
        EditText txtDate = findViewById(R.id.in_date);
        EditText txtTime = findViewById(R.id.in_time);
        DateSelectors.setDatePickerDialog(this, findViewById(R.id.btn_date), txtDate);
        DateSelectors.setTimePickerDialog(this, findViewById(R.id.btn_time), txtTime);
        saveButton.setOnClickListener(v -> {
            final String chosenTitle = eventTitle.getText().toString();
            final GPSCoordinates chosenCoordinates = new GPSCoordinates(mapFragment.getMovingMarkerPosition());
            final String chosenDescription = eventDescription.getText().toString();

            final long date = DateSelectors.parseEditTextTimeAndDate(txtDate, txtTime);

            // validation
            if (EventCreationValidator.eventCreationValidation(eventTitle, eventDescription)) {
                saveEvent(chosenTitle, chosenCoordinates, chosenDescription, date);
                this.finish();
            }
        });
    }

    /**
     * Saves the event into the view model.
     *
     * @param title       (String): title of the event
     * @param coordinates (GPSCoordinates): position of the event
     * @param description (String): description of the event
     */
    private void saveEvent(String title, GPSCoordinates coordinates, String description, long date) {
        AuthenticatedUser user = new GoogleAuth().loggedUser();
        String ownerId = (user == null) ? EditProfileActivity.NULL_USER : user.uid;

        //Create associated event.
        Event newEvent = new Event(eventCreatorViewModel.getUniqueId(), title, description, coordinates, ownerId, new ArrayList<>(singletonList(ownerId)), new ArrayList<>(), date);
        //Save the event & return to previous activity.
        if(eventCreatorViewModel.saveEvent(newEvent)) {
            //TODO add yourself to the participants by default?
            EventNotificationManager manager = new EventNotificationManager();
            manager.subscribeToEventTopic(newEvent.getId());

            System.out.println("\n\n\n\nSubscribed to this event : " + newEvent.getId() + "\n\n\n\n");
        }
    }
}