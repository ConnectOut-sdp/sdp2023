package com.sdpteam.connectout.event.viewer;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.sdpteam.connectout.R;

public abstract class MapViewFragment extends Fragment implements OnMapReadyCallback {

    public static final int DEFAULT_MAP_ZOOM = 15;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this); // callback to call when the map is ready

        updateDataView(rootView);

        return rootView;
    }

    /**
     * Method used to update different components on the map.
     *
     * @param rootView (View): initial view of the fragment.
     */
    public abstract void updateDataView(View rootView);
}
