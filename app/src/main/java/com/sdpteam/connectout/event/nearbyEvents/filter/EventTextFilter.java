package com.sdpteam.connectout.event.nearbyEvents.filter;

import com.sdpteam.connectout.event.Event;

public class EventTextFilter implements EventFilter {

    private final String text;

    public EventTextFilter(String text) {
        this.text = text.trim().toLowerCase();
    }

    @Override
    public boolean test(Event event) {
        return (event.getTitle() + event.getDescription()).toLowerCase().contains(text.toLowerCase());
    }
}
