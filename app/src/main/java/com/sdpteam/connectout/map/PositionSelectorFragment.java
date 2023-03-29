package com.sdpteam.connectout.map;

import static com.google.android.gms.maps.GoogleMap.OnMarkerDragListener;

import androidx.annotation.NonNull;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.EventsViewModel;

import java.util.List;

public class PositionSelectorFragment extends MapViewFragment implements OnMapReadyCallback {

    //Movable marker used to get event location
    private Marker movingMarker;
    private GoogleMap map;

    public PositionSelectorFragment(EventsViewModel mapViewModel) {
        super(mapViewModel);
    }


    @Override
    public void showNewMarkerList(List<Event> eventList) {
        if (map == null) {
            return;
        }
        super.showNewMarkerList(eventList);
        MarkerOptions markerOptions = new MarkerOptions()
                .position(getMovingMarkerPosition())
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        movingMarker = map.addMarker(markerOptions);
    }

    /**
     * @inheritDoc
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        map = googleMap;
        super.onMapReady(map);
        //Create the marker, and mark it as movable
        MarkerOptions markerOptions = new MarkerOptions()
                .position(new LatLng(0, 0))
                .draggable(true)
                .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
        movingMarker = map.addMarker(markerOptions);
        //Upon moving, update the marker's true position.
        map.setOnMarkerDragListener(new OnMarkerDragListener() {


            @Override
            public void onMarkerDrag(@NonNull Marker marker) {
                movingMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_YELLOW));
            }

            @Override
            public void onMarkerDragEnd(@NonNull Marker marker) {
                movingMarker.setPosition(marker.getPosition());
                movingMarker.setIcon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_BLUE));
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