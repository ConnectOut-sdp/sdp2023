package com.sdpteam.connectout;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.widget.Switch;

import com.sdpteam.connectout.drawer.FilterFragment;
import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.mapList.MapListModel;
import com.sdpteam.connectout.mapList.MapListModelManager;
import com.sdpteam.connectout.mapList.list.ListViewFragment;
import com.sdpteam.connectout.mapList.map.MapViewFragment;
import com.sdpteam.connectout.mapList.MapListViewModel;
import com.sdpteam.connectout.mapList.MapListViewModelFactory;

import java.util.List;

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