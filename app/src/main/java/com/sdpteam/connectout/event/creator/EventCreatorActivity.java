package com.sdpteam.connectout.event.creator;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.AuthenticatedUser;
import com.sdpteam.connectout.authentication.GoogleAuth;
import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.EventFirebaseDataSource;
import com.sdpteam.connectout.event.nearbyEvents.map.GPSCoordinates;
import com.sdpteam.connectout.profile.EditProfileActivity;
import com.sdpteam.connectout.utils.WithFragmentActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.icu.util.Calendar;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.widget.Toolbar;

import java.util.Date;
import java.util.GregorianCalendar;

public class EventCreatorActivity extends WithFragmentActivity {
    private EventCreatorViewModel eventCreatorViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creator);

        if (eventCreatorViewModel == null) {
            eventCreatorViewModel = new EventCreatorViewModel(new EventFirebaseDataSource());
        }

        Toolbar toolbar = findViewById(R.id.event_creator_toolbar);
        setSupportActionBar(toolbar);

        LocationPicker mapFragment = new LocationPicker(eventCreatorViewModel);
        replaceFragment(mapFragment, R.id.event_creator_fragment_container);

        toolbar.setNavigationOnClickListener(v -> this.finish());

        Button saveButton = findViewById(R.id.event_creator_save_button);

        EditText eventTitle = findViewById(R.id.event_creator_title);
        EditText eventDescription = findViewById(R.id.event_creator_description);
        EditText txtDate = findViewById(R.id.in_date);
        EditText txtTime = findViewById(R.id.in_time);

        setDatePickerDialog(findViewById(R.id.btn_date), txtDate);
        setTimePickerDialog(findViewById(R.id.btn_time), txtTime);
        saveButton.setOnClickListener(v -> {
            final String chosenTitle = eventTitle.getText().toString();
            final GPSCoordinates chosenCoordinates = new GPSCoordinates(mapFragment.getMovingMarkerPosition());
            final String chosenDescription = eventDescription.getText().toString();
            final String[] yearMonthDay = txtDate.getText().toString().split("-"); //[day, month, year]
            final String[] hourMin = txtTime.getText().toString().split(":"); //[hour, min]
            final Date date;
            if (yearMonthDay.length == 3 && hourMin.length == 2){
                date = new GregorianCalendar(Integer.valueOf(yearMonthDay[2]), Integer.valueOf(yearMonthDay[1])-1,
                        Integer.valueOf(yearMonthDay[0]), Integer.valueOf(hourMin[0]),
                        Integer.valueOf(hourMin[1])).getTime();
            }
            else{
                //TODO should fail if the time is not appropriate (No event shouldn't have a time)
                //we said that we would do this in a future sprint task
                date = null;
            }
            saveEvent(chosenTitle, chosenCoordinates, chosenDescription, date);
            this.finish();
        });
    }

    /**
     * Saves the event into the view model.
     *
     * @param title       (String): title of the event
     * @param coordinates (GPSCoordinates): position of the event
     * @param description (String): description of the event
     */
    private void saveEvent(String title, GPSCoordinates coordinates, String description, Date date) {
        AuthenticatedUser user = new GoogleAuth().loggedUser();
        String ownerId = user == null ? EditProfileActivity.NULL_USER : user.uid;

        //Create associated event.
        //TODO add yourself to the participants by default?
        Event newEvent = new Event(eventCreatorViewModel.getUniqueId(), title, description, coordinates, ownerId,null, (date != null)? date.getTime(): 0);

        //Save the event & return to previous activity.
        eventCreatorViewModel.saveEvent(newEvent);
    }

    private void setDatePickerDialog(Button btnDatePicker, EditText txtDate){
        btnDatePicker.setOnClickListener(v -> {
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);
            DatePickerDialog datePickerDialog = new DatePickerDialog(this,
                    (view, year1, monthOfYear, dayOfMonth) -> txtDate.setText(dayOfMonth + "-" + (monthOfYear + 1) + "-" + year1), year, month, day);
            datePickerDialog.show();
        });
    }

    private void setTimePickerDialog(Button btnTimePicker, EditText txtTime){
        btnTimePicker.setOnClickListener(v -> {
            // Get Current Time
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);
            // Launch Time Picker Dialog
            TimePickerDialog timePickerDialog = new TimePickerDialog(this,
                    (view, hourOfDay, minute1) -> txtTime.setText(hourOfDay + ":" + minute1), hour, minute, false);
            timePickerDialog.show();
        });
    }
}