package com.sdpteam.connectout;

/**
 * This class describes an event
 * Every single marker on the map corresponds to a different one
 */
public class Event {

    private String title;
    private double lat;
    private double lng;

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
