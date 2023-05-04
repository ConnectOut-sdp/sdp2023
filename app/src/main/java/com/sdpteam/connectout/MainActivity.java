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

        // Don't Change anything in there anymore.
        Intent drawerIntent = new Intent(getApplicationContext(), DrawerActivity.class);
        this.startActivity(drawerIntent);

    }

    /**
     * Enables persistence for offline queries
     */
    private void enablingFirebaseCache() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}