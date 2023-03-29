package com.sdpteam.connectout;

import com.sdpteam.connectout.authentication.GoogleLoginActivity;
import com.sdpteam.connectout.event.nearbyEvents.EventsActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        // Don't put anything here, just choose which activity to redirect to
        Intent drawerIntent = new Intent(getApplicationContext(), EventsActivity.class);
        this.startActivity(drawerIntent);
    }
}