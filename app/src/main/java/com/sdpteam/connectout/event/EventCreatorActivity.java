package com.sdpteam.connectout.event;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.WithFragmentActivity;
import com.sdpteam.connectout.map.MapCreatorFragment;

public class EventCreatorActivity extends WithFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creator);

        //Retrieve tool bar and give it the control.
        Toolbar toolbar = findViewById(R.id.event_creator_toolbar);
        setSupportActionBar(toolbar);

        //set up the map fragment.
        MapCreatorFragment mapFragment = new MapCreatorFragment();
        replaceFragment(mapFragment, R.id.event_creator_fragment_container);

        //Upon click on the tool bar icon, end this activity and return to previous one.
        toolbar.setNavigationOnClickListener(v -> this.finish());

        EditText eventTitle = findViewById(R.id.event_creator_title);
        EditText eventDescription = findViewById(R.id.event_creator_description);
        Button saveButton = findViewById(R.id.event_creator_save_button);
        saveButton.setOnClickListener(v ->
                Log.v("EventText", mapFragment.markerToEvent()
                                                   .setTitle(eventTitle.getText().toString())
                                                   .setDescription(eventDescription.getText().toString())
                                                   .build()
                                                   .toString()));


    }


}