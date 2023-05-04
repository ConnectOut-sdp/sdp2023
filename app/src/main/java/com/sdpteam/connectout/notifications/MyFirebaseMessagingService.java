package com.sdpteam.connectout.notifications;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sdpteam.connectout.MainActivity;
import com.sdpteam.connectout.R;

import java.util.Date;
import java.util.Map;

/**
 * Class that deals with sending notifications
 */
public class MyFirebaseMessagingService extends FirebaseMessagingService {

    @Override
    public void onMessageReceived(@NonNull RemoteMessage remoteMessage) {
        super.onMessageReceived(remoteMessage);

        if (remoteMessage.getNotification() != null) {
            String title = remoteMessage.getNotification().getTitle();
            String text = remoteMessage.getNotification().getBody();
            sendNotification(title, text);
        }
    }

    private void sendNotification(String title, String text) {
        int notificationId = (int) (System.currentTimeMillis() % 10000);

        // Opens up the MainActivity when user clicks on the notification
        Intent intent = new Intent(this, MainActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP); // ensuring only one instance of 'MainActivity' is opened
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_ONE_SHOT);

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, Constants.NOTIFICATION_CHANNEL_ID)
//                .setSmallIcon(R.drawable.your_notification_icon) // Replace with your app logo
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return; // must activate notification on mobile phone
        }
        notificationManager.notify(notificationId, notificationBuilder.build());
    }
}

