package com.sdpteam.connectout.map;

import static com.google.android.gms.maps.GoogleMap.*;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sdpteam.connectout.R;
import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.EventBuilder;

import java.util.List;

public class MapCreatorFragment extends Fragment implements OnMapReadyCallback {

    private MarkerOptions markerOptions;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.activity_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        return rootView;
    }



    /**
     * Manipulates the map once available.
     * This callback is triggered when the map is ready to be used.
     * This is where we can add markers or lines, add listeners or move the camera.
     * If Google Play services is not installed on the device, the user will be prompted to install
     * it inside the SupportMapFragment. This method will only be triggered once the user has
     * installed Google Play services and returned to the app.
     */
    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        markerOptions = new MarkerOptions()
                .position(new LatLng(0, 0))
                .draggable(true);
        Marker marker = googleMap.addMarker(markerOptions);
        googleMap.setOnMarkerDragListener(new OnMarkerDragListener() {


            @Override
            public void onMarkerDrag(@NonNull Marker marker) {

            }

            @Override
            public void onMarkerDragEnd(@NonNull Marker marker) {
                    markerOptions.position(marker.getPosition());
            }

            @Override
            public void onMarkerDragStart(@NonNull Marker marker) {

            }
        });

    }

    /**
     * Uses the marker current position to give the position of the associated event
     * @return (EventBuilder): Incomplete description of the event selected
     */
    public EventBuilder markerToEvent(){
        return new EventBuilder(markerOptions.getPosition().latitude, markerOptions.getPosition().longitude);
    }



}