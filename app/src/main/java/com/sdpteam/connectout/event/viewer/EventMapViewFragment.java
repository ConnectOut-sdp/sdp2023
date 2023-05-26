package com.sdpteam.connectout.event.viewer;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.sdpteam.connectout.R;
import com.sdpteam.connectout.event.Event;

import android.view.View;
import androidx.annotation.NonNull;

public class EventMapViewFragment extends MapViewFragment {

    private final EventViewModel eventViewModel;
    private GoogleMap map;

    public EventMapViewFragment(EventViewModel eventViewModel) {
        this.eventViewModel = eventViewModel;
        eventViewModel.refreshEvent();
    }

    @Override
    public void updateDataView(View rootView) {
        eventViewModel.refreshEvent();
        eventViewModel.getEventLiveData().observe(getViewLifecycleOwner(), this::showEventOnMap);

        FloatingActionButton refreshButton = rootView.findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(view -> eventViewModel.refreshEvent());
    }

    public void showEventOnMap(Event event) {
        if (map == null) {
            return;
        }
        map.clear();
        MarkerOptions m = new MarkerOptions().position(event.getCoordinates().toLatLng()).title(event.getTitle());
        map.addMarker(m);
        map.animateCamera(CameraUpdateFactory.newLatLngZoom(m.getPosition(), DEFAULT_MAP_ZOOM));
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
    }
}
