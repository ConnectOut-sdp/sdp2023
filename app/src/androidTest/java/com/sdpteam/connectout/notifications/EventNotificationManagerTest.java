package com.sdpteam.connectout.notifications;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;


public class EventNotificationManagerTest {

    public List<String> subscribers = new ArrayList<>();

    private EventNotificationManager eventNotificationManager;

    @Before
    public void setUp() {
        eventNotificationManager = new EventNotificationManager();
      }

    @Test
    public void testSubscribeToEventTopic() {
        String eventId = "test";
        eventNotificationManager.subscribeToEventTopic(eventId);
        subscribers.add("one");
        assertEquals(1, subscribers.size());
    }

    @Test
    public void testUnsubscribeFromEventTopic() {
        String eventId = "test";
        eventNotificationManager.unsubscribeFromEventTopic(eventId);
        subscribers.remove("one");
        assertEquals(0, subscribers.size());
    }

}
