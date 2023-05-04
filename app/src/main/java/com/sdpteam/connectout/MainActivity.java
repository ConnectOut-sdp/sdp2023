package com.sdpteam.connectout;

import com.google.firebase.database.FirebaseDatabase;
import com.sdpteam.connectout.drawer.DrawerActivity;

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


        // Don't put anything here, just choose which activity to redirect to
        Intent drawerIntent = new Intent(getApplicationContext(), DrawerActivity.class);
        this.startActivity(drawerIntent);
    }

    private void enablingFirebaseCache() {
        // enabling persistence for offline queries
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}