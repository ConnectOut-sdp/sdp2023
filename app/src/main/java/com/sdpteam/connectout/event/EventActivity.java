package com.sdpteam.connectout.event;

import static java.lang.String.format;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.map.GPSCoordinates;
import com.sdpteam.connectout.profile.ProfileID;

import java.util.Locale;
import java.util.Set;

public class EventActivity extends AppCompatActivity {

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

    }

    private Event getEvent() {
        // TODO retrieve event from ID using the view-model
        return new Event("Some title", new GPSCoordinates(37.7749, -122.4194), "Some description");
    }

    private void showParticipants(Set<ProfileID> participants) {
        // TODO launch new activity with list of profiles
    }

    private void joinEvent(Event event) {
        // TODO persistence
        finish();
    }
}
