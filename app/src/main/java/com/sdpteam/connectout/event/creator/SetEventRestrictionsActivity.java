package com.sdpteam.connectout.event.creator;

import static com.sdpteam.connectout.event.viewer.EventActivity.PASSED_ID_KEY;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.EventFirebaseDataSource;
import com.sdpteam.connectout.event.viewer.EventViewModel;
import com.sdpteam.connectout.validation.EventCreationValidator;

public class SetEventRestrictionsActivity extends AppCompatActivity {
    private EventViewModel eventViewModel;

    private String eventId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_set_event_restrictions);
        eventId = getIntent().getStringExtra(PASSED_ID_KEY);

        if (eventViewModel == null) {
            eventViewModel = new EventViewModel(new EventFirebaseDataSource());
        }
       // Toolbar toolbar = findViewById(R.id.set_event_restrictions_toolbar);
       // setSupportActionBar(toolbar);
       // toolbar.setNavigationOnClickListener(v -> this.finish());
        Button saveButton = findViewById(R.id.set_event_restrictions_save_button);

        EditText eventMinRating = findViewById(R.id.set_event_restrictions_min_rating);
        EditText eventMaxNumParticipants = findViewById(R.id.set_event_restrictions_max_num_participants);
        //EditText txtDate = findViewById(R.id.in_date);
        //EditText txtTime = findViewById(R.id.in_time);
        //setDatePickerDialog(findViewById(R.id.btn_date), txtDate);
        //setTimePickerDialog(findViewById(R.id.btn_time), txtTime);

        saveButton.setOnClickListener(v -> {
            final double chosenMinRating = Double.parseDouble(eventMinRating.getText().toString());
            final int chosenMaxNumParticipants = Integer.parseInt(eventMaxNumParticipants.getText().toString());

            // validation
            if(EventCreationValidator.eventRestrictionsValidation(chosenMinRating, chosenMaxNumParticipants)) {
                saveEventRestrictions(new Event.EventRestrictions(chosenMinRating, chosenMaxNumParticipants));
                this.finish();
            }
        });
    }

    private void saveEventRestrictions(Event.EventRestrictions restrictions){
        eventViewModel.saveEventRestrictions(eventId, restrictions);
    }
}