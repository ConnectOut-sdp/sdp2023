package com.sdpteam.connectout.event.nearbyEvents.filter;

import com.sdpteam.connectout.event.Event;

public class CalendarEventFilter implements EventFilter {
    String profileId;

    public CalendarEventFilter(String profileId) {
        this.profileId = profileId;
    }

    @Override
    public boolean test(Event event) {
        return event.getParticipants().contains(profileId);
    }
}
