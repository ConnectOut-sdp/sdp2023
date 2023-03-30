package com.sdpteam.connectout.event.nearbyEvents;

import android.os.Bundle;
import android.widget.Button;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.event.EventFirebaseDataSource;
import com.sdpteam.connectout.event.nearbyEvents.filter.EventsFilterDialog;
import com.sdpteam.connectout.event.nearbyEvents.list.EventsListViewFragment;
import com.sdpteam.connectout.event.nearbyEvents.map.EventsMapViewFragment;
import com.sdpteam.connectout.utils.WithFragmentActivity;

public class EventsActivity extends WithFragmentActivity {

    private EventsMapViewFragment eventsMapViewFragment;
    private EventsListViewFragment eventsListViewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        EventsViewModel viewModel = new ViewModelProvider(this, new EventsViewModelFactory(new EventFirebaseDataSource())).get(EventsViewModel.class);
        eventsMapViewFragment = new EventsMapViewFragment(viewModel);
        eventsListViewFragment = new EventsListViewFragment(viewModel);

        final Toolbar toolbar = findViewById(R.id.events_toolbar);
        setSupportActionBar(toolbar);
        final Button filterBtn = findViewById(R.id.events_filter_button);

        filterBtn.setOnClickListener(v -> {
            final EventsFilterDialog filterDialog = new EventsFilterDialog(viewModel);
            filterDialog.show(getSupportFragmentManager(), "FilterDialog");
        });

        RadioGroup mapListButton = findViewById(R.id.events_switch);
        replaceFragment(eventsMapViewFragment, R.id.nearby_events_container);
        RadioButton mapButton = findViewById(R.id.map_switch_button);
        RadioButton listButton = findViewById(R.id.list_switch_button);

        mapListButton.setOnCheckedChangeListener(toggleViewMode(mapButton, listButton));
    }

    private RadioGroup.OnCheckedChangeListener toggleViewMode(RadioButton mapButton, RadioButton listButton) {
        return (buttonView, isChecked) -> {
            if (R.id.map_switch_button == isChecked) {
                listButton.setChecked(false);
                replaceFragment(eventsMapViewFragment, R.id.nearby_events_container);
            } else {
                mapButton.setChecked(false);
                replaceFragment(eventsListViewFragment, R.id.nearby_events_container);
            }
        };
    }
}