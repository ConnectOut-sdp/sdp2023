package com.sdpteam.connectout.event.nearbyEvents;

import static androidx.test.core.app.ApplicationProvider.getApplicationContext;

import static com.sdpteam.connectout.event.creator.EventCreatorActivity.openEventCreator;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.event.EventFirebaseDataSource;
import com.sdpteam.connectout.event.creator.EventCreatorActivity;
import com.sdpteam.connectout.event.nearbyEvents.filter.EventsFilterDialog;
import com.sdpteam.connectout.event.nearbyEvents.list.EventsListViewFragment;
import com.sdpteam.connectout.event.nearbyEvents.map.EventsMapViewFragment;
import com.sdpteam.connectout.utils.DrawerFragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.lifecycle.ViewModelProvider;

public class EventsFragment extends DrawerFragment {

    private EventsMapViewFragment eventsMapViewFragment;
    private EventsListViewFragment eventsListViewFragment;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_events, container, false);

        EventsViewModel viewModel = new ViewModelProvider(requireActivity(), new EventsViewModelFactory(new EventFirebaseDataSource())).get(EventsViewModel.class);
        eventsMapViewFragment = new EventsMapViewFragment(viewModel);
        eventsListViewFragment = new EventsListViewFragment(viewModel);

        Button filterBtn = view.findViewById(R.id.events_filter_button);
        Toolbar toolbar = view.findViewById(R.id.events_toolbar);
        View.OnClickListener listener = v -> {
            final EventsFilterDialog filterDialog = new EventsFilterDialog(viewModel);
            filterDialog.show(getParentFragmentManager(), "FilterDialog");
        };

        setupToolBar(filterBtn, toolbar, "Filter", listener);

        RadioGroup mapListButton = view.findViewById(R.id.events_switch);
        getChildFragmentManager().beginTransaction().replace(R.id.nearby_events_container, eventsMapViewFragment).commit();

        RadioButton mapButton = view.findViewById(R.id.map_switch_button);
        RadioButton listButton = view.findViewById(R.id.list_switch_button);
        ImageButton mapAddButton = view.findViewById(R.id.events_map_add_button);
        Button listAddButton = view.findViewById(R.id.events_list_add_button);

        mapListButton.setOnCheckedChangeListener(toggleViewMode(mapButton, listButton, mapAddButton, listAddButton));

        mapAddButton.setOnClickListener(v -> openEventCreator(getContext()));
        listAddButton.setOnClickListener(v -> openEventCreator(getContext()));

        return view;
    }

    private RadioGroup.OnCheckedChangeListener toggleViewMode(RadioButton mapButton, RadioButton listButton, ImageButton mapAddButton, Button listAddButton) {
        return (buttonView, isChecked) -> {
            if (R.id.map_switch_button == isChecked) {
                listButton.setChecked(false);
                listButton.setTextColor(Color.BLACK);
                mapButton.setTextColor(Color.WHITE);
                mapAddButton.setVisibility(View.VISIBLE);
                listAddButton.setVisibility(View.GONE);
                getChildFragmentManager().beginTransaction().replace(R.id.nearby_events_container, eventsMapViewFragment).commit();
            } else {
                mapButton.setChecked(false);
                listButton.setTextColor(Color.WHITE);
                mapButton.setTextColor(Color.BLACK);
                listAddButton.setVisibility(View.VISIBLE);
                mapAddButton.setVisibility(View.GONE);
                getChildFragmentManager().beginTransaction().replace(R.id.nearby_events_container, eventsListViewFragment).commit();
            }
        };
    }
}

