package com.sdpteam.connectout;

import static androidx.fragment.app.FragmentManager.TAG;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sdpteam.connectout.authentication.GoogleLoginActivity;
import com.sdpteam.connectout.drawer.DrawerActivity;
import com.sdpteam.connectout.notifications.Constants;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        enablingFirebaseCache();
        createNotificationChannel();

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
        Intent drawerIntent = new Intent(getApplicationContext(), GoogleLoginActivity.class);
        this.startActivity(drawerIntent);
    }

    private void enablingFirebaseCache() {
        // enabling persistence for offline queries
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = Constants.NOTIFICATION_NAME;
            String description = Constants.NOTIFICATION_DESCRIPTION;
            String channelId = Constants.NOTIFICATION_CHANNEL_ID;
            int importance = NotificationManager.IMPORTANCE_HIGH;
            NotificationChannel channel = new NotificationChannel(channelId, name, importance);
            channel.setDescription(description);
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
    }

}