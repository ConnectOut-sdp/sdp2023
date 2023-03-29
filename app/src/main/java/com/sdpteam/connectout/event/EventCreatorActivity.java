package com.sdpteam.connectout.event;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.WithFragmentActivity;
import com.sdpteam.connectout.authentication.AuthenticatedUser;
import com.sdpteam.connectout.authentication.GoogleAuth;
import com.sdpteam.connectout.map.GPSCoordinates;
import com.sdpteam.connectout.map.PositionSelectorFragment;
import com.sdpteam.connectout.profile.EditProfileActivity;

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

        PositionSelectorFragment mapFragment = new PositionSelectorFragment(eventCreatorViewModel);
        replaceFragment(mapFragment, R.id.event_creator_fragment_container);

        toolbar.setNavigationOnClickListener(v -> this.finish());

        Button saveButton = findViewById(R.id.event_creator_save_button);

        EditText eventTitle = findViewById(R.id.event_creator_title);
        EditText eventDescription = findViewById(R.id.event_creator_description);

        saveButton.setOnClickListener(v ->
        {
            saveEvent(eventTitle.getText().toString(),
                    new GPSCoordinates(mapFragment.getMovingMarkerPosition()),
                    eventDescription.getText().toString()
            );
            this.finish();
        });


    }

    /**
     * Saves the event into the view model.
     *
     * @param title       (String): title of the event
     * @param coordinates (GPSCoordinates): position of the event
     * @param description (String): description of the event
     */
    private void saveEvent(String title, GPSCoordinates coordinates, String description) {
        AuthenticatedUser user = new GoogleAuth().loggedUser();
        String ownerId = user == null ? EditProfileActivity.NULL_USER : user.uid;

        //Create associated event.
        Event newEvent = new Event(
                eventCreatorViewModel.getUniqueId(),
                title,
                description,
                coordinates,
                ownerId
        );

        //Save the event & return to previous activity.
        eventCreatorViewModel.saveEvent(newEvent);
    }


}