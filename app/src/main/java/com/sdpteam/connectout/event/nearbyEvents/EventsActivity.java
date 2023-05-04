package com.sdpteam.connectout.event.nearbyEvents;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.utils.WithFragmentActivity;

import android.os.Bundle;

public class EventsActivity extends WithFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        replaceFragment(new EventsFragment(), R.id.fragment_container);
    }
}