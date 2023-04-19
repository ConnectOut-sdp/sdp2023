package com.sdpteam.connectout.event.nearbyEvents.map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;

import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sdpteam.connectout.R;
import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.location.LocationHelper;
import com.sdpteam.connectout.event.nearbyEvents.EventsViewModel;

import java.util.List;
import java.util.stream.Collectors;

public class EventsMapViewFragment extends Fragment implements OnMapReadyCallback {

    private static final int DEFAULT_MAP_ZOOM = 15;
    private final EventsViewModel eventsViewModel;
    private GoogleMap map;

    public EventsMapViewFragment(EventsViewModel eventsViewModel) {
        this.eventsViewModel = eventsViewModel;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this); // callback to call when the map is ready

        eventsViewModel.refreshEvents();

        eventsViewModel.getEventListLiveData().observe(getViewLifecycleOwner(), this::showEventsOnMap);

        ImageButton refreshButton = rootView.findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(view -> eventsViewModel.refreshEvents());

        return rootView;
    }

    public void showEventsOnMap(List<Event> eventList) {
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
        if (ActivityCompat.checkSelfPermission(getContext(), ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            map.setMyLocationEnabled(true);
        }

        LocationHelper.getInstance(getContext()).getLastLocation(getActivity(), location -> {
            if (location == null) {
                return;
            }
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), DEFAULT_MAP_ZOOM));
        });
    }
}