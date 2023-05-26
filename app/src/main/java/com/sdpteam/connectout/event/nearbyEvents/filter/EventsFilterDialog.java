package com.sdpteam.connectout.event.nearbyEvents.filter;

import static com.sdpteam.connectout.utils.DateSelectors.dateToCalendar;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.event.location.LocationHelper;
import com.sdpteam.connectout.event.nearbyEvents.EventsViewModel;
import com.sdpteam.connectout.event.nearbyEvents.map.GPSCoordinates;
import com.sdpteam.connectout.utils.DateSelectors;
import com.sdpteam.connectout.validation.EventValidator;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.DialogFragment;

public class EventsFilterDialog extends DialogFragment {

    private static final String SEEKBAR_ALL_DISTANCES = "all distances";
    private static String TEXT_FILTER = "";
    private static int SEEKBAR_VALUE = 100;
    private static String DATE_VALUE = "";
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
            locationHelper.getLastLocation(getActivity(), location -> position = GPSCoordinates.fromLocation(location));
        }
        final EditText txtDate = view.findViewById(R.id.filter_in_date);
        txtDate.setText(DATE_VALUE);
        DateSelectors.setDatePickerDialog(getContext(), view.findViewById(R.id.filter_btn_date), txtDate);

        applyBtn.setOnClickListener(v -> applyFilter(search, txtDate, seekBar));
        return view;
    }

    private void applyFilter(EditText search, EditText txtDate, SeekBar seekBar) {
        final EventFilter textFilter = new EventTextFilter(search.getText().toString());
        final EventFilter locationFilter = new EventLocationFilter(position, seekBar.getProgress());

        EventFilter dateFilter = setupSelectedDate(txtDate);
        final EventFilter eventFilter = textFilter.and(locationFilter).and(dateFilter);

        eventsViewModel.setFilter(eventFilter);
        eventsViewModel.refreshEvents();

        // save filter state before dismiss dialog
        SEEKBAR_VALUE = seekBar.getProgress();
        TEXT_FILTER = search.getText().toString();
        dismiss();
    }

    private EventDateFilter setupSelectedDate(EditText txtDate) {
        EventDateFilter dateFilter = new EventDateFilter(null);
        String dateString = txtDate.getText().toString();
        if (dateString.equals("")) {
            DATE_VALUE = dateString;
        } else if (EventValidator.isValidFormat(dateString, EventValidator.DATE_FORMAT)) {
            dateFilter = new EventDateFilter(dateToCalendar(txtDate));
            DATE_VALUE = dateString;
        } else {
            Toast.makeText(getContext(), "Warning ! Date filter is invalid...", Toast.LENGTH_SHORT).show();
        }
        return dateFilter;
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
