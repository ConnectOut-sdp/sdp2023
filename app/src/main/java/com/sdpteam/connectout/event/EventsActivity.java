package com.sdpteam.connectout.event;

import android.os.Bundle;
import android.widget.RadioButton;
import android.widget.RadioGroup;

import androidx.lifecycle.ViewModelProvider;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.eventList.ListViewFragment;
import com.sdpteam.connectout.map.MapViewFragment;
import com.sdpteam.connectout.utils.WithFragmentActivity;

public class EventsActivity extends WithFragmentActivity {

    private MapViewFragment mapViewFragment;
    private ListViewFragment listViewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);


        RadioGroup mapListButton = findViewById(R.id.events_switch);

        EventsViewModel viewModel = new ViewModelProvider(this, new EventsViewModelFactory(new EventFirebaseDataSource())).get(EventsViewModel.class);
        mapViewFragment = new MapViewFragment(viewModel);
        listViewFragment = new ListViewFragment();
        replaceFragment(mapViewFragment, R.id.events_map_list_container);
        RadioButton mapButton = findViewById(R.id.map_switch_button);
        RadioButton listButton = findViewById(R.id.list_switch_button);

        mapListButton.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (R.id.map_switch_button == isChecked) {
                        listButton.setChecked(false);
                        replaceFragment(mapViewFragment, R.id.events_map_list_container);
                    } else {
                        mapButton.setChecked(false);
                        replaceFragment(listViewFragment, R.id.events_map_list_container);
                    }
                }

        );

    }
}