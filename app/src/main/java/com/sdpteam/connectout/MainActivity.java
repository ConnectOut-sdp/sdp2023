package com.sdpteam.connectout;

import android.content.Intent;
import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.database.FirebaseDatabase;
import com.sdpteam.connectout.drawer.DrawerActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enablingFirebaseCache();

        // Don't put anything here, just choose which activity to redirect to
        Intent drawerIntent = new Intent(getApplicationContext(), DrawerActivity.class);
        this.startActivity(drawerIntent);
    }

    private void enablingFirebaseCache() {
        // enabling persistence for offline queries
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}