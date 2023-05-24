package com.sdpteam.connectout;

import com.google.firebase.database.FirebaseDatabase;
import com.sdpteam.connectout.authentication.GoogleLoginActivity;
import com.sdpteam.connectout.notifications.NotificationService;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enablingFirebaseCache();

        NotificationService service = new NotificationService();
        service.createNotificationChannel();

        Intent drawerIntent = new Intent(getApplicationContext(), GoogleLoginActivity.class);

        this.finish();
        this.startActivity(drawerIntent);
    }

    /**
     * Enables persistence for offline queries
     */
    private void enablingFirebaseCache() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}