package com.sdpteam.connectout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Switch;

import androidx.lifecycle.ViewModelProvider;

import com.sdpteam.connectout.mapList.MapListModel;
import com.sdpteam.connectout.mapList.MapListViewModel;
import com.sdpteam.connectout.mapList.MapListViewModelFactory;
import com.sdpteam.connectout.mapList.list.ListViewFragment;
import com.sdpteam.connectout.mapList.map.MapViewFragment;

public class EventsMapListActivity extends WithFragmentActivity {

    private MapViewFragment mapViewFragment;
    private ListViewFragment listViewFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_events);

        @SuppressLint("UseSwitchCompatOrMaterialCode")
        Switch mapListSwitch = findViewById(R.id.events_switch);

        MapListViewModel viewModel = new ViewModelProvider(this, new MapListViewModelFactory(new MapListModel())).get(MapListViewModel.class);
        mapViewFragment = new MapViewFragment(viewModel);
        listViewFragment = new ListViewFragment();
        replaceFragment(mapViewFragment, R.id.events_map_list_container);

        mapListSwitch.setOnCheckedChangeListener((buttonView, isChecked) -> {
                    if (!isChecked) {
                        replaceFragment(mapViewFragment, R.id.events_map_list_container);
                        System.out.println("switch is checked");
                    } else {
                        replaceFragment(listViewFragment, R.id.events_map_list_container);
                        System.out.println("switch unchecked");
                    }
                }

        );

    }
}