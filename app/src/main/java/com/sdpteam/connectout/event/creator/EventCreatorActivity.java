package com.sdpteam.connectout.event.creator;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.event.EventFirebaseDataSource;
import com.sdpteam.connectout.event.nearbyEvents.map.GPSCoordinates;
import com.sdpteam.connectout.remoteStorage.ImageSelectionFragment;
import com.sdpteam.connectout.utils.DateSelectors;
import com.sdpteam.connectout.utils.WithFragmentActivity;
import com.sdpteam.connectout.validation.EventCreationValidator;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

public class EventCreatorActivity extends WithFragmentActivity {
    private EventCreatorViewModel eventCreatorViewModel;
    private Uri selectedImageUri;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creator);

        if (eventCreatorViewModel == null) {
            eventCreatorViewModel = new EventCreatorViewModel(new EventFirebaseDataSource());
        }
        Toolbar toolbar = findViewById(R.id.event_creator_toolbar);
        setSupportActionBar(toolbar);
        LocationPicker mapFragment = new LocationPicker();
        replaceFragment(mapFragment, R.id.event_creator_fragment_container);
        toolbar.setNavigationOnClickListener(v -> this.finish());
        Button saveButton = findViewById(R.id.event_creator_save_button);

        EditText eventTitle = findViewById(R.id.event_creator_title);
        EditText eventDescription = findViewById(R.id.event_creator_description);
        EditText txtDate = findViewById(R.id.in_date);
        EditText txtTime = findViewById(R.id.in_time);
        DateSelectors.setDatePickerDialog(this, findViewById(R.id.btn_date), txtDate);
        DateSelectors.setTimePickerDialog(this, findViewById(R.id.btn_time), txtTime);
        saveButton.setOnClickListener(v -> {
            final String chosenTitle = eventTitle.getText().toString();
            final GPSCoordinates chosenCoordinates = new GPSCoordinates(mapFragment.getMovingMarkerPosition());
            final String chosenDescription = eventDescription.getText().toString();

            final long date = DateSelectors.parseEditTextTimeAndDate(txtDate, txtTime);

            // validation
            if (EventCreationValidator.eventCreationValidation(eventTitle, eventDescription, txtDate, txtTime)) {
                saveButton.setText("Saving..");
                eventCreatorViewModel.uploadImage(selectedImageUri).thenAccept((uploadImageUrl) -> {
                    saveButton.setText("Saving...");
                    eventCreatorViewModel.saveEvent(chosenTitle, chosenCoordinates, chosenDescription, date, uploadImageUrl);
                    this.finish();
                });
            }
        });

        setupImageSelector();
    }

    private void setupImageSelector() {
        ImageSelectionFragment imageSelectionFragment = new ImageSelectionFragment(R.drawable.mountain_image);
        imageSelectionFragment.setOnImageSelectedListener(imageUri -> this.selectedImageUri = imageUri);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.event_creator_selectImage, imageSelectionFragment);
        transaction.commit();
    }

    public static void openEventCreator(Context fromContext) {
        Intent creationIntent = new Intent(fromContext, EventCreatorActivity.class);
        fromContext.startActivity(creationIntent);
    }
}