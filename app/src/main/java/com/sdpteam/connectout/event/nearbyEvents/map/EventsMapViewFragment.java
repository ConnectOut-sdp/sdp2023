package com.sdpteam.connectout.event.nearbyEvents.map;

import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static com.sdpteam.connectout.event.viewer.EventMapViewFragment.DEFAULT_MAP_ZOOM;

import android.content.pm.PackageManager;
import android.view.View;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sdpteam.connectout.R;
import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.location.LocationHelper;
import com.sdpteam.connectout.event.nearbyEvents.EventsViewModel;
import com.sdpteam.connectout.event.viewer.EventActivity;
import com.sdpteam.connectout.event.viewer.MapViewFragment;

import java.util.List;
import java.util.stream.Collectors;

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

        ImageButton refreshButton = rootView.findViewById(R.id.refresh_button);
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
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        map.setOnMarkerClickListener(marker -> {
            EventActivity.openEvent(getContext(), (String) marker.getTag());
            return false;
        });

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