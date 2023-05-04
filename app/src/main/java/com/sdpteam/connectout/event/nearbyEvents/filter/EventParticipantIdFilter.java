package com.sdpteam.connectout.event.nearbyEvents.filter;

import com.sdpteam.connectout.event.Event;

/**
 * Filters participants by their ids.
 */
public class EventParticipantIdFilter implements EventFilter {

    private final String userId;

    public EventParticipantIdFilter(String userId) {

        this.userId = userId;
    }

    @Override
    public boolean test(Event event) {
        if (userId == null) {
            return false;
        }
        return userId.equals(event.getOrganizer()) || event.hasJoined(userId);
    }
}
