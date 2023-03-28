package com.sdpteam.connectout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Switch;
import android.widget.ToggleButton;

import androidx.lifecycle.ViewModelProvider;

import com.sdpteam.connectout.event.EventFirebaseDataSource;
import com.sdpteam.connectout.event.EventsViewModel;
import com.sdpteam.connectout.event.EventsViewModelFactory;
import com.sdpteam.connectout.eventList.ListViewFragment;
import com.sdpteam.connectout.map.MapViewFragment;

public class EventsMapListActivity extends WithFragmentActivity {

    private MapViewFragment mapViewFragment;
    private ListViewFragment listViewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);


        ToggleButton mapListButton = findViewById(R.id.events_switch);

        EventsViewModel viewModel = new ViewModelProvider(this, new EventsViewModelFactory(new EventFirebaseDataSource())).get(EventsViewModel.class);
        mapViewFragment = new MapViewFragment(viewModel);
        listViewFragment = new ListViewFragment();
        replaceFragment(mapViewFragment, R.id.events_map_list_container);

        mapListButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (!isChecked) {
                        replaceFragment(mapViewFragment, R.id.events_map_list_container);
                    } else {
                        replaceFragment(listViewFragment, R.id.events_map_list_container);
                    }
                }

        );

    }
}