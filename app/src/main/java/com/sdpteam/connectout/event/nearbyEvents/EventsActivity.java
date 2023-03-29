package com.sdpteam.connectout.event.nearbyEvents;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.event.EventFirebaseDataSource;
import com.sdpteam.connectout.event.nearbyEvents.list.EventsListViewFragment;
import com.sdpteam.connectout.event.nearbyEvents.map.EventsMapViewFragment;
import com.sdpteam.connectout.utils.WithFragmentActivity;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.lifecycle.ViewModelProvider;

public class EventsActivity extends WithFragmentActivity {

    private EventsMapViewFragment eventsMapViewFragment;
    private EventsListViewFragment eventsListViewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        RadioGroup mapListButton = findViewById(R.id.events_switch);

        EventsViewModel viewModel = new ViewModelProvider(this, new EventsViewModelFactory(new EventFirebaseDataSource())).get(EventsViewModel.class);
        eventsMapViewFragment = new EventsMapViewFragment(viewModel);
        eventsListViewFragment = new EventsListViewFragment(viewModel);

        replaceFragment(eventsMapViewFragment, R.id.nearby_events_container);
        RadioButton mapButton = findViewById(R.id.map_switch_button);
        RadioButton listButton = findViewById(R.id.list_switch_button);

        mapListButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (R.id.map_switch_button == isChecked) {
                        listButton.setChecked(false);
                        replaceFragment(eventsMapViewFragment, R.id.nearby_events_container);
                    } else {
                        mapButton.setChecked(false);
                        replaceFragment(eventsListViewFragment, R.id.nearby_events_container);
                    }
                }

        );
    }
}