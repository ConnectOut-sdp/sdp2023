package com.sdpteam.connectout.event;

import static java.lang.String.format;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModelProvider;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.WithFragmentActivity;
import com.sdpteam.connectout.mapList.map.GPSCoordinates;
import com.sdpteam.connectout.mapList.MapListModelManager;
import com.sdpteam.connectout.mapList.MapListViewModel;
import com.sdpteam.connectout.mapList.map.MapViewFragment;
import com.sdpteam.connectout.mapList.MapListViewModelFactory;

import java.util.Collections;
import java.util.List;
import java.util.Locale;

public class EventActivity extends WithFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event);

        final Event event = getEvent();

        final TextView title = findViewById(R.id.event_title);
        final TextView description = findViewById(R.id.event_description);
        final Button joinBtn = findViewById(R.id.event_join_button);
        final Button participantsBtn = findViewById(R.id.event_participants_button);

        title.setText(event.getTitle());
        description.setText(event.getDescription());
        joinBtn.setOnClickListener(v -> joinEvent(event));

        final String participantsBtnText = format(Locale.getDefault(), "%s (%d)", participantsBtn.getText(), event.getParticipants().size());
        participantsBtn.setText(participantsBtnText);
        participantsBtn.setOnClickListener(v -> showParticipants(event.getParticipants()));

        initMapFragment(event);
    }

    private void initMapFragment(Event event) {

        // Model that returns a singleton of the event as the event list
        final MapListModelManager mapModel = (String filteredAttribute, String expectedValue) -> {
            final MutableLiveData<List<Event>> data = new MutableLiveData<>();
            data.setValue(Collections.singletonList(event)); // only show one marker in the map
            return data;
        };
        // Implicitly instantiating MapListViewModel to use that instance back in MapViewFragment
        final MapListViewModel mapViewModel = new ViewModelProvider(this, new MapListViewModelFactory(mapModel)).get(MapListViewModel.class);


        final MapViewFragment map = new MapViewFragment(mapViewModel);
        replaceFragment(map, R.id.event_fragment_container);
    }

    private Event getEvent() {
        // TODO retrieve event from ID using the view-model
        return new Event("a", "Some title", "Some description", new GPSCoordinates(37.7749, -122.4194), "toto");
    }

    private void showParticipants(List<String> participants) {
        // TODO launch new activity (or pop-up) with list of profiles
    }

    private void joinEvent(Event event) {
        // TODO persistence
        finish();
    }
}
