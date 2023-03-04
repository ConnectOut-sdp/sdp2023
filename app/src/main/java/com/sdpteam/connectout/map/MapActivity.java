package com.sdpteam.connectout.map;

import java.util.List;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.sdpteam.connectout.R;

import android.os.Bundle;
import android.widget.Button;
import androidx.fragment.app.FragmentActivity;
import androidx.lifecycle.ViewModelProvider;

public class MapActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private Button refreshButton;

    //container object for the map used for the map lifecycle management and UI capabilities
    private SupportMapFragment mapFragment;
    private MapViewModel mapViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_map);

        // Obtain the SupportMapFragment and get notified when the map is ready to be used.
        mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);

        mapViewModel = new ViewModelProvider(this).get(MapViewModel.class);
        mapViewModel.init(new MapModel());

        mapViewModel.getEventList().observe(this, eventList -> showNewMarkerList(eventList));

        refreshButton = (Button) findViewById(R.id.refresh_button);
        refreshButton.setOnClickListener(view -> showNewMarkerList(mapViewModel.refreshEventList().getValue()));
    }

    private void showNewMarkerList(List<Event> eventList) {
        if (mMap == null) {
            //throw new IllegalStateException();
            return;
        }
        mMap.clear();
        for (Event e : eventList) {
            LatLng ll = new LatLng(e.getLat(), e.getLng());
            MarkerOptions m = new MarkerOptions().position(ll).title(e.getTitle());
            mMap.addMarker(m);
        }
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
    public void onMapReady(GoogleMap googleMap) {
        mapViewModel.onMapReady(googleMap);
        mMap = googleMap;

        // Add a marker in Sydney and move the camera
        /*  THIS IS THE BOOTCAMP STUFF, IM COMMENTING IT BECAUSE IT MAY BE CAUSING THE COVERAGE TO BE EXTREMELY LOW
        LatLng satellite = new LatLng(46.520544, 6.567825);
        MarkerOptions m = new MarkerOptions().position(satellite).title("Satellite");
        mMap.addMarker(m);
        // The zoom value is between 1 and 20 with 1 being a world zoom and 20 being a building scale zoom
        // 15 is a good value to see the city around an individual
        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(46.520536, 6.568318), 15));

        mMap.setOnMarkerClickListener(marker -> {
            // on marker click we are getting the title of our marker
            // which is clicked and displaying it in a toast message.
            String markerName = marker.getTitle();
            Toast.makeText(MapActivity.this, "Clicked location is " + markerName + ": coordinates are Lat " + marker.getPosition().latitude + "and Lng " + marker.getPosition().longitude, Toast
            .LENGTH_SHORT).show();
            return false;
        });*/
    }
}