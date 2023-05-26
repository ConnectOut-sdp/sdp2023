package com.sdpteam.connectout.notifications;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.sdpteam.connectout.MainActivity;

import android.Manifest;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

/**
 * Class that deals with sending notifications
 */
public class NotificationService extends FirebaseMessagingService {

    public static final String NOTIFICATION_NAME = "your_channel_name";
    public static final String NOTIFICATION_DESCRIPTION = "your_channel_description";
    public static final String NOTIFICATION_CHANNEL_ID = "your_channel_id";

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

        NotificationCompat.Builder notificationBuilder = new NotificationCompat.Builder(this, NotificationService.NOTIFICATION_CHANNEL_ID)
//                .setSmallIcon(R.drawable.your_notification_icon) // Replace with your app logo // todo add a notification logo for our app
                .setContentTitle(title)
                .setContentText(text)
                .setAutoCancel(true)
                .setContentIntent(pendingIntent)
                .setPriority(NotificationCompat.PRIORITY_HIGH);

        NotificationManagerCompat notificationManager = NotificationManagerCompat.from(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.POST_NOTIFICATIONS) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        notificationManager.notify(notificationId, notificationBuilder.build());
    }

    /**
     * Method to create a notification channel for the application
     */
    public void createNotificationChannel() {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                CharSequence name = NotificationService.NOTIFICATION_NAME;
                String description = NotificationService.NOTIFICATION_DESCRIPTION;
                String channelId = NotificationService.NOTIFICATION_CHANNEL_ID;
                int importance = NotificationManager.IMPORTANCE_HIGH;
                NotificationChannel channel = new NotificationChannel(channelId, name, importance);
                channel.setDescription(description);
                NotificationManager notificationManager = getSystemService(NotificationManager.class);
                notificationManager.createNotificationChannel(channel);
            }
        } catch (Exception e) {
            // do nothing
        }
    }
}

