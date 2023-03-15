package com.sdpteam.connectout.event;

import static java.lang.String.format;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.WithFragmentActivity;
import com.sdpteam.connectout.map.GPSCoordinates;
import com.sdpteam.connectout.map.MapViewFragment;
import com.sdpteam.connectout.map.MapViewModel;
import com.sdpteam.connectout.map.MapViewModelFactory;
import com.sdpteam.connectout.profile.ProfileID;

import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Set;

public class EventActivity extends WithFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        final Event event = getEvent();

        final TextView title = findViewById(R.id.event_title);
        final TextView description = findViewById(R.id.event_description);
        final Button participants = findViewById(R.id.event_participants_button);
        final Button join = findViewById(R.id.event_join_button);

        title.setText(event.getTitle());
        description.setText(event.getDescription());
        participants.setText(format(Locale.getDefault(), "%s (%d)", participants.getText(), event.getParticipants().size()));
        participants.setOnClickListener(v -> showParticipants(event.getParticipants()));
        join.setOnClickListener(v -> joinEvent(event));

        initMapFragment(event);
    }

    private void initMapFragment(Event event) {
        final Fragment map = new MapViewFragment();
        replaceFragment(map, R.id.event_fragment_container);

        final MapViewModelFactory viewModelFactory = new MapViewModelFactory(() -> {
            final MutableLiveData<List<Event>> data = new MutableLiveData<>();
            data.setValue(Collections.singletonList(event)); // only show one marker in the map
            return data;
        });

        // Implicitly instantiating MapViewModel to use that instance back in MapViewFragment
        new ViewModelProvider(this, viewModelFactory).get(MapViewModel.class);
    }

    private Event getEvent() {
        // TODO retrieve event from ID using the view-model
        return new Event("Some title", new GPSCoordinates(37.7749, -122.4194), "Some description");
    }

    private void showParticipants(Set<ProfileID> participants) {
        // TODO launch new activity (or pop-up) with list of profiles
    }

    private void joinEvent(Event event) {
        // TODO persistence
        finish();
    }
}
