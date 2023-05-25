package com.sdpteam.connectout.event.nearbyEvents.map;

import static com.sdpteam.connectout.event.location.LocationHelper.toLatLng;

import java.util.List;
import java.util.stream.Collectors;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sdpteam.connectout.R;
import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.location.LocationHelper;
import com.sdpteam.connectout.event.nearbyEvents.EventsViewModel;
import com.sdpteam.connectout.event.viewer.EventActivity;
import com.sdpteam.connectout.event.viewer.MapViewFragment;

import android.annotation.SuppressLint;
import android.view.View;
import androidx.annotation.NonNull;

public class EventsMapViewFragment extends MapViewFragment {

    private final EventsViewModel eventsViewModel;
    private GoogleMap map;

    public EventsMapViewFragment(EventsViewModel eventsViewModel) {
        this.eventsViewModel = eventsViewModel;
    }

    @Override
    public void updateDataView(View rootView) {
        eventsViewModel.refreshEvents();

        eventsViewModel.getEventListLiveData().observe(getViewLifecycleOwner(), this::showEventsOnMap);

        FloatingActionButton refreshButton = rootView.findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(view -> eventsViewModel.refreshEvents());
    }

    public void showEventsOnMap(List<Event> eventList) {
        if (map == null) {
            return;
        }
        map.clear();
        eventList = eventList.stream().filter(e -> e.getCoordinates() != null).collect(Collectors.toList());

        for (Event e : eventList) {
            MarkerOptions m = new MarkerOptions().position(e.getCoordinates().toLatLng()).title(e.getTitle());
            map.addMarker(m).setTag(e.getId());
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
    @SuppressLint("MissingPermission")
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setOnMarkerClickListener(marker -> {
            EventActivity.openEvent(getContext(), (String) marker.getTag());
            return false;
        });

        final LocationHelper locationHelper = LocationHelper.getInstance(getContext());

        if (locationHelper.hasPermission(getActivity())) {
            map.setMyLocationEnabled(true);
        }

        locationHelper.getLastLocation(getActivity(), location -> {
            if (location == null) {
                return;
            }
            map.moveCamera(CameraUpdateFactory.newLatLngZoom(toLatLng(location), DEFAULT_MAP_ZOOM));
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        eventsViewModel.refreshEvents();
    }
}