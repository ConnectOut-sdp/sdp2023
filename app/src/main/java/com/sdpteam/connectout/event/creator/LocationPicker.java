package com.sdpteam.connectout.event.creator;

import static com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_BLUE;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.HUE_YELLOW;
import static com.google.android.gms.maps.model.BitmapDescriptorFactory.defaultMarker;

import android.view.View;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sdpteam.connectout.event.location.LocationHelper;
import com.sdpteam.connectout.event.viewer.MapViewFragment;

public class LocationPicker extends MapViewFragment {

    //Movable marker used to get event location
    private Marker movingMarker;
    private GoogleMap map;

    @Override
    public void updateDataView(View rootView) {
        showMarkerOnMap();
    }

    public void showMarkerOnMap() {
        if (map == null) {
            return;
        }
        MarkerOptions markerOptions = new MarkerOptions()
                .position(getMovingMarkerPosition())
                .draggable(true)
                .icon(defaultMarker(HUE_BLUE));
        movingMarker = map.addMarker(markerOptions);
    }

    /**
     * @inheritDoc
     */
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;

        //Create the marker, and mark it as movable

        LocationHelper.getInstance(getContext()).getLastLocation(getActivity(), location -> {
            MarkerOptions markerOptions = new MarkerOptions().position(new LatLng(0, 0)).draggable(true).icon(defaultMarker(HUE_BLUE));
            if (location != null) {
                final LatLng latlng = LocationHelper.toLatLng(location);
                markerOptions.position(latlng);
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(latlng, DEFAULT_MAP_ZOOM));
            }
            movingMarker = map.addMarker(markerOptions);
        });

        //Upon moving, update the marker's true position.
        map.setOnMarkerDragListener(new OnMarkerDragListener() {

            @Override
            public void onMarkerDrag(@NonNull Marker marker) {
                movingMarker.setIcon(defaultMarker(HUE_YELLOW));
            }

            @Override
            public void onMarkerDragEnd(@NonNull Marker marker) {
                movingMarker.setPosition(marker.getPosition());
                movingMarker.setIcon(defaultMarker(HUE_BLUE));
                map.moveCamera(CameraUpdateFactory.newLatLng(marker.getPosition()));
            }

            @Override
            public void onMarkerDragStart(@NonNull Marker marker) {

            }
        });
    }

    /**
     * Gives the movable marker last recorded position
     *
     * @return (LatLng): Position of the movable marker
     */
    public LatLng getMovingMarkerPosition() {
        return movingMarker == null ? new LatLng(0, 0) : movingMarker.getPosition();
    }
}