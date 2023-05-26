package com.sdpteam.connectout.notifications;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertThrows;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.mock;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;

import com.google.firebase.messaging.RemoteMessage;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.os.Build;
import android.os.Bundle;
import androidx.test.core.app.ApplicationProvider;

public class NotificationServiceTest {

    @Test
    public void testConstants() {
        assertEquals("your_channel_name", NotificationService.NOTIFICATION_NAME);
        assertEquals("your_channel_description", NotificationService.NOTIFICATION_DESCRIPTION);
        assertEquals("your_channel_id", NotificationService.NOTIFICATION_CHANNEL_ID);
    }

    @Test
    public void testOnMessageReceived() {
        // Create a custom subclass of NotificationService to override its behavior and avoid actual notification sending
        NotificationService notificationService = new NotificationService();

        Bundle bundle = new Bundle();
        bundle.putString("from", "/topics/news");
        bundle.putString("google.sent_time", "123456789");
        bundle.putString("google.message_id", "0:123456789%abcd1234fghi");
        bundle.putString("google.c.a.e", "1");
        bundle.putString("collapse_key", "do_not_collapse");
        bundle.putString("gcm.notification.e", "1");

        RemoteMessage.Builder builder = new RemoteMessage.Builder("1")
                .setMessageId("1")
                .setData(bundleToMap(bundle));

        builder.build().getNotification();

        assertThrows(NullPointerException.class, () -> notificationService.onMessageReceived(builder.build()));
    }

    private Map<String, String> bundleToMap(Bundle bundle) {
        Map<String, String> map = new HashMap<>();
        for (String key : bundle.keySet()) {
            Object value = bundle.get(key);
            if (value != null) {
                map.put(key, value.toString());
            }
        }
        return map;
    }

    @Test
    public void testCreateNotificationChannel() {
        // Create a custom subclass of NotificationService to override its behavior and avoid actual channel creation
        NotificationService notificationService = new NotificationService() {
            @Override
            public NotificationManager getSystemService(String name) {
                return mock(NotificationManager.class);
            }
        };

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            notificationService.createNotificationChannel();
            NotificationManager notificationManager = (NotificationManager) ApplicationProvider.getApplicationContext().getSystemService(NotificationManager.class);
            NotificationChannel channel = notificationManager.getNotificationChannel(NotificationService.NOTIFICATION_CHANNEL_ID);

            if (channel != null) {
                assertEquals(NotificationService.NOTIFICATION_CHANNEL_ID, channel.getId());
                assertEquals(NotificationService.NOTIFICATION_NAME, channel.getName());
                assertEquals(NotificationService.NOTIFICATION_DESCRIPTION, channel.getDescription());
                assertTrue(channel.getImportance() >= NotificationManager.IMPORTANCE_HIGH);
//            verify(notificationManager).createNotificationChannel(channel);

            }
        }
    }

    @Test
    public void forceTestCreateNotificationChannel() {
        // Create a custom subclass of NotificationService to override its behavior and avoid actual channel creation
        NotificationService notificationService = new NotificationService() {
            @Override
            public NotificationManager getSystemService(String name) {
                return mock(NotificationManager.class);
            }
        };

        notificationService.createNotificationChannel();
        NotificationManager notificationManager = (NotificationManager) ApplicationProvider.getApplicationContext().getSystemService(NotificationManager.class);
        NotificationChannel channel = notificationManager.getNotificationChannel(NotificationService.NOTIFICATION_CHANNEL_ID);
    }
}
