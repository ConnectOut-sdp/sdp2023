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
            eventCreatorViewModel = new EventCreatorViewModel(new EventCreatorModel());
        }

        //Retrieve tool bar and give it the control.
        Toolbar toolbar = findViewById(R.id.event_creator_toolbar);
        setSupportActionBar(toolbar);

        //set up the map fragment.
        PositionSelectorFragment mapFragment = new PositionSelectorFragment();
        replaceFragment(mapFragment, R.id.event_creator_fragment_container);

        //Upon click on the tool bar icon, end this activity and return to previous one.
        toolbar.setNavigationOnClickListener(v -> this.finish());

        //Find given save button.
        Button saveButton = findViewById(R.id.event_creator_save_button);

        //Find given title and description.
        EditText eventTitle = findViewById(R.id.event_creator_title);
        EditText eventDescription = findViewById(R.id.event_creator_description);


        //upon save, log the current event and return to previous activity.
        saveButton.setOnClickListener(v ->
        {saveEvent(eventTitle.getText().toString(),
                        new GPSCoordinates(mapFragment.getMovingMarkerPosition()),
                        eventDescription.getText().toString()
                );
            this.finish();
        });


    }

    /**
     *  Saves the event into the view model.
     *
     * @param title (String): title of the event
     * @param coordinates (GPSCoordinates): position of the event
     * @param description (String): description of the event
     */
    private void saveEvent(String title, GPSCoordinates coordinates, String description) {
        AuthenticatedUser user = new GoogleAuth().loggedUser();
        String ownerId = user == null ? EditProfileActivity.NULL_USER : user.uid;

        //Create associated event.
        Event newEvent = new Event(
                title,
                coordinates,
                description,
                ownerId,
                eventCreatorViewModel.getUniqueId()
        );

        //Save the event & return to previous activity.
        eventCreatorViewModel.saveEvent(newEvent);
    }


}