package com.sdpteam.connectout;

import com.sdpteam.connectout.event.creator.EventCreatorActivity;
import com.sdpteam.connectout.event.viewer.RegisteredEventsCalendarActivity;
import com.sdpteam.connectout.profile.ProfileActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Don't put anything here, just choose which activity to redirect to

        Intent drawerIntent = new Intent(getApplicationContext(), ProfileActivity.class);
        this.startActivity(drawerIntent);
    }
}