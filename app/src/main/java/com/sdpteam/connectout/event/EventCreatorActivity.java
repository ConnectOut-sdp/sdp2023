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

import java.util.UUID;

public class EventCreatorActivity extends WithFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creator);

        //Retrieve tool bar and give it the control.
        Toolbar toolbar = findViewById(R.id.event_creator_toolbar);
        setSupportActionBar(toolbar);

        //set up the map fragment.
        PositionSelectorFragment mapFragment = new PositionSelectorFragment();
        replaceFragment(mapFragment, R.id.event_creator_fragment_container);

        //Upon click on the tool bar icon, end this activity and return to previous one.
        toolbar.setNavigationOnClickListener(v -> this.finish());

        //upon save, log the current event and return to previous activity.
        Button saveButton = findViewById(R.id.event_creator_save_button);

        EditText eventTitle = findViewById(R.id.event_creator_title);
        EditText eventDescription = findViewById(R.id.event_creator_description);

        saveButton.setOnClickListener(v -> {
            AuthenticatedUser user = new GoogleAuth().loggedUser();
            String uid = user == null ? EditProfileActivity.NULL_USER : user.uid;
            Event newEvent = new Event(eventTitle.getText().toString(), new GPSCoordinates(mapFragment.getMovingMarkerPosition()), eventDescription.getText().toString(), uid, UUID.randomUUID().toString());
            new EventCreatorViewModel(new EventCreatorModel()).saveValue(newEvent);
            this.finish();

        });


    }


}