package com.sdpteam.connectout;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.messaging.FirebaseMessaging;
import com.sdpteam.connectout.authentication.GoogleLoginActivity;
import com.sdpteam.connectout.drawer.DrawerActivity;
import com.sdpteam.connectout.notifications.NotificationService;
import com.sdpteam.connectout.post.view.PostsActivity;

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

        // Don't Change anything in there anymore.
        Intent drawerIntent = new Intent(getApplicationContext(), PostsActivity.class); // DrawerActivity
        this.startActivity(drawerIntent);
    }

    /**
     * Enables persistence for offline queries
     */
    private void enablingFirebaseCache() {
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);
    }
}