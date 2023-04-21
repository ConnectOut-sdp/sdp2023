package com.sdpteam.connectout;

import com.google.firebase.database.FirebaseDatabase;
import com.sdpteam.connectout.event.creator.EventCreatorActivity;
import com.sdpteam.connectout.event.viewer.RegisteredEventsCalendarActivity;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enablingFirebaseCache();

        // Don't put anything here, just choose which activity to redirect to
        Intent drawerIntent = new Intent(getApplicationContext(), EventCreatorActivity.class);
        this.startActivity(drawerIntent);
    }

    private void enablingFirebaseCache() {
        // enabling persistence for offline queries
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}