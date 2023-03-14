package com.sdpteam.connectout.event;

import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.WithFragmentActivity;
import com.sdpteam.connectout.map.PositionSelectorFragment;

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
        saveButton.setOnClickListener(v ->
                returnSavedResults());


    }


    //TODO : send the returned results of the save to the firebase.
    private void returnSavedResults() {

    }


}