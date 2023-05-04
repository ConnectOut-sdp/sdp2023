package com.sdpteam.connectout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sdpteam.connectout.drawer.DrawerActivity;
import com.sdpteam.connectout.notifications.NotificationService;


import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;


public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enablingFirebaseCache();


        NotificationService service = new NotificationService();
        service.createNotificationChannel();

        FirebaseMessaging.getInstance().subscribeToTopic("event_TESTING").addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                String msg = "Done";
                if(!task.isSuccessful()) {
                    msg = "Failed";
                }
                System.out.println("msg");
            }
        });


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