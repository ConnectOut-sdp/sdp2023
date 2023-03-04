package com.sdpteam.connectout.map;

/**
 * This class describes an event
 * Every single marker on the map corresponds to a different one
 */
public class Event {

    private final String title;
    private final double lat;
    private final double lng;

    public Event(String title, double lat, double lng) {
        this.title = title;
        this.lat = lat;
        this.lng = lng;
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
}
