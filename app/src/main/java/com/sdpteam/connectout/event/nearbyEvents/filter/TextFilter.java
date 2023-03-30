package com.sdpteam.connectout.event.nearbyEvents.filter;

import com.sdpteam.connectout.event.Event;

public class TextFilter implements EventFilter {

    private final String text;

    public TextFilter(String text) {
        this.text = text.trim().toLowerCase();
    }

    @Override
    public boolean test(Event event) {
        return (event.getTitle() + event.getDescription()).toLowerCase().contains(text);
    }
}
