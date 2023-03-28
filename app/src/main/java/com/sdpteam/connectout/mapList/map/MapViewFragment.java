package com.sdpteam.connectout.mapList.map;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sdpteam.connectout.R;
import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.EventsViewModel;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MapViewFragment extends Fragment implements OnMapReadyCallback {

    private GoogleMap map;
    private final EventsViewModel mapViewModel;
    private static final int DEFAULT_MAP_ZOOM = 15;

    public MapViewFragment(EventsViewModel mapViewModel) {
        this.mapViewModel = mapViewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        //container object for the map used for the map lifecycle management and UI capabilities
        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapViewModel.getEventList().observe(getViewLifecycleOwner(), this::showNewMarkerList);

        ImageButton refreshButton = rootView.findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(view -> showNewMarkerList(mapViewModel.refreshEventList().getValue()));

        return rootView;
    }


    public void showNewMarkerList(List<Event> eventList) {
        if (map == null) {
            return;
        }
        map.clear();
        eventList = eventList.stream().filter(e -> e.getCoordinates() != null).collect(Collectors.toList());

        for (Event e : eventList) {
            MarkerOptions m = new MarkerOptions().position(e.getCoordinates().toLatLng()).title(e.getTitle());
            map.addMarker(m);
        }
    }

    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     * Also shows the markers instead of explicitly having to click on the refresh button
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        showNewMarkerList(mapViewModel.refreshEventList().getValue());
    }
}