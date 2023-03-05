package com.sdpteam.connectout.event;

import androidx.annotation.NonNull;

/**
 * This class describes an event
 * Every single marker on the map corresponds to a different one
 */
public class Event {

    private final String title;
    private final double lat;
    private final double lng;
    private final String description;

    public Event(String title, double lat, double lng, String description) {
        this.title = title;
        this.lat = lat;
        this.lng = lng;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public double getLat() {
        return lat;
    }

    public double getLng() {
        return lng;
    }

    @NonNull
    @Override
    public String toString() {
        return title+"\n is at "+lat +" and "+ lng;

    }
}
