package com.sdpteam.connectout.notifications;

import com.google.firebase.messaging.FirebaseMessaging;

public class EventNotificationManager {
    public void subscribeToEventTopic(String eventId) {
        FirebaseMessaging.getInstance().subscribeToTopic("event_" + eventId);
    }

    public void unsubscribeFromEventTopic(String eventId) {
        FirebaseMessaging.getInstance().unsubscribeFromTopic("event_" + eventId);
    }
}
