package com.sdpteam.connectout.event.creator;

import static com.sdpteam.connectout.event.viewer.EventActivity.PASSED_ID_KEY;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.EventFirebaseDataSource;
import com.sdpteam.connectout.event.viewer.EventViewModel;
import com.sdpteam.connectout.utils.DateSelectors;
import com.sdpteam.connectout.validation.EventValidator;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

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
        Toolbar toolbar = findViewById(R.id.set_event_restrictions_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> this.finish());
        Button saveButton = findViewById(R.id.set_event_restrictions_save_button);

        EditText eventMinRating = findViewById(R.id.set_event_restrictions_min_rating);
        EditText eventMaxNumParticipants = findViewById(R.id.set_event_restrictions_max_num_participants);
        EditText txtDate = findViewById(R.id.set_event_restrictions_in_date);
        EditText txtTime = findViewById(R.id.set_event_restrictions_in_time);

        DateSelectors.setDatePickerDialog(this, findViewById(R.id.set_event_restrictions_btn_date), txtDate);
        DateSelectors.setTimePickerDialog(this, findViewById(R.id.set_event_restrictions_btn_time), txtTime);

        saveButton.setOnClickListener(v -> {
            if (EventValidator.eventRestrictionsValidation(eventMinRating, eventMaxNumParticipants, txtDate, txtTime)) {
                final double chosenMinRating = Double.parseDouble(eventMinRating.getText().toString());
                final int chosenMaxNumParticipants = Integer.parseInt(eventMaxNumParticipants.getText().toString());
                final long chosenDate = DateSelectors.parseEditTextTimeAndDate(txtDate, txtTime);
                saveEventRestrictions(new Event.EventRestrictions(chosenMinRating, chosenMaxNumParticipants, chosenDate));
                this.finish();
            }
        });
    }

    private void saveEventRestrictions(Event.EventRestrictions restrictions) {
        eventViewModel.saveEventRestrictions(eventId, restrictions);
    }

    public static void openRestrictions(Context context, String eventId) {
        final Intent intent = new Intent(context, SetEventRestrictionsActivity.class);
        intent.putExtra(PASSED_ID_KEY, eventId);
        context.startActivity(intent);
    }
}