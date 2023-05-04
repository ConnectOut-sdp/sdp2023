package com.sdpteam.connectout.event.nearbyEvents.filter;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.event.location.LocationHelper;
import com.sdpteam.connectout.event.nearbyEvents.EventsViewModel;
import com.sdpteam.connectout.event.nearbyEvents.map.GPSCoordinates;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import androidx.fragment.app.DialogFragment;

public class EventsFilterDialog extends DialogFragment {

    private static String TEXT_FILTER = "";
    private static int SEEKBAR_VALUE = 100;
    private static String SEEKBAR_ALL_DISTANCES = "all distances";
    private final EventsViewModel eventsViewModel;
    private GPSCoordinates position;

    public EventsFilterDialog(EventsViewModel eventsViewModel) {
        this.eventsViewModel = eventsViewModel;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.popup_events_filter, container, false);

        final EditText search = view.findViewById(R.id.events_filter_search);
        search.setText(TEXT_FILTER);
        final SeekBar seekBar = view.findViewById(R.id.events_filter_seekbar);
        seekBar.setProgress(SEEKBAR_VALUE);
        seekBar.setOnSeekBarChangeListener(seekBarHandler(view));
        final TextView progressLabel = view.findViewById(R.id.events_filter_seekbar_value);
        progressLabel.setText(SEEKBAR_ALL_DISTANCES);
        final Button applyBtn = view.findViewById(R.id.events_filter_apply_btn);

        final LocationHelper locationHelper = LocationHelper.getInstance(getContext());
        if (locationHelper.hasPermission(getActivity())) {
            locationHelper.getLastLocation(getActivity(), location -> {
                position = GPSCoordinates.fromLocation(location);
            });
        }

        applyBtn.setOnClickListener(v -> applyFilter(search, seekBar));
        return view;
    }

    private void applyFilter(EditText search, SeekBar seekBar) {
        final EventFilter textFilter = new EventTextFilter(search.getText().toString());
        final EventFilter locationFilter = new EventLocationFilter(position, seekBar.getProgress());
        final EventFilter eventFilter = textFilter.and(locationFilter);

        eventsViewModel.setFilter(eventFilter, ProfilesFilter.NONE);
        eventsViewModel.refreshEvents();

        // save filter state before dismiss dialog
        SEEKBAR_VALUE = seekBar.getProgress();
        TEXT_FILTER = search.getText().toString();
        dismiss();
    }

    private SeekBar.OnSeekBarChangeListener seekBarHandler(View view) {
        return new SeekBar.OnSeekBarChangeListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                final TextView progressLabel = view.findViewById(R.id.events_filter_seekbar_value);
                if (progress == 100) { // acts like a disabled filter
                    progressLabel.setText(SEEKBAR_ALL_DISTANCES);
                    return;
                }
                progressLabel.setText(progress + "km");
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
            }
        };
    }
}
