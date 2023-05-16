package com.sdpteam.connectout.event.viewer;

import android.os.Bundle;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.utils.WithFragmentActivity;

/**
 * This activity sets up the Calendar UserInterface
 * It consists in the list of events to which a user has registered
 */
public class MyEventsCalendarActivity extends WithFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_container);
        replaceFragment(new MyEventsCalendarFragment(), R.id.fragment_container);
    }
}