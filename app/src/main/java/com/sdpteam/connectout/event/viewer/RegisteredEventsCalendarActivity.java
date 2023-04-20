package com.sdpteam.connectout.event.viewer;

import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;

import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.Gravity;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.database.FirebaseListOptions;
import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.Authentication;
import com.sdpteam.connectout.authentication.GoogleAuth;
import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileFirebaseDataSource;
import com.sdpteam.connectout.profile.ProfileViewModel;

import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

/**
 * This activity sets up the Calendar UserInterface
 * It consists in the list of events to which a user has registered
 */
public class RegisteredEventsCalendarActivity extends AppCompatActivity {
    public ProfileViewModel viewModel = new ProfileViewModel(new ProfileFirebaseDataSource());
    Authentication auth = new GoogleAuth();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registered_events_calendar);

        //we enable the return button in the ActionBar
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        //set up the ListView
        setUpListAdapter();

        ListView listOfRegisteredEvents = findViewById(R.id.list_of_registered_events);

        //on click, open event
        listOfRegisteredEvents.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                TextView item = view.findViewById(R.id.registered_event_id);

                Intent intent = new Intent(RegisteredEventsCalendarActivity.this, EventActivity.class);
                intent.putExtra("key", item.getText());
                startActivity(intent);
            }
        });
    }

    /**
     * Returns to the previous activity if the return button in the Actionbar is pressed
     */
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            this.finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * The view must not interact directly with Firebase, as such the FirebaseListAdapter is created
     * by the model.
     * However the adapter needs indirect access to the view elements as such we pass lambdas so that
     * the Model has no direct access.
     */
    private void setUpListAdapter() {
        ListView listOfRegisteredEvents = findViewById(R.id.list_of_registered_events);
        listOfRegisteredEvents.setStackFromBottom(true);

        Function<FirebaseListOptions.Builder<Profile.CalendarEvent>, FirebaseListOptions.Builder<Profile.CalendarEvent>> setLayout = a -> a.setLayout(R.layout.registered_event);
        Function<FirebaseListOptions.Builder<Profile.CalendarEvent>, FirebaseListOptions.Builder<Profile.CalendarEvent>> setLifecycleOwner = a -> a.setLifecycleOwner(this);
        BiConsumer<View, Profile.CalendarEvent> populateView = (v, calendarEvent) -> {
            TextView registeredEventTitle = v.findViewById(R.id.registered_event_title);
            TextView registeredEventTime = v.findViewById(R.id.registered_event_time);
            TextView registeredEventId = v.findViewById(R.id.registered_event_id);

            registeredEventTitle.setText(calendarEvent.getEventTitle());
            registeredEventId.setText(calendarEvent.getEventId());
            // Format the date before showing it
            registeredEventTime.setText(DateFormat.format("dd-MM-yyyy (HH:mm:ss)",
                    calendarEvent.getEventDate()));
            registeredEventTime.setGravity(Gravity.CENTER);
            registeredEventTitle.setGravity(Gravity.CENTER);
        };
        Consumer<ListAdapter> setAdapter = adapter -> listOfRegisteredEvents.setAdapter(adapter);
        String profileId = auth.isLoggedIn() ? auth.loggedUser().uid : NULL_USER;
        viewModel.setUpListAdapter(setLayout, setLifecycleOwner, populateView, setAdapter, profileId);
    }
}