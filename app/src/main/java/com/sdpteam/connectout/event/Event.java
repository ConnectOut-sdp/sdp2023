package com.sdpteam.connectout.event;

import com.sdpteam.connectout.map.GPSCoordinates;

/**
 * This class describes an event
 * Every single marker on the map corresponds to a different one
 */
public class Event {

    private final String title;
    private final GPSCoordinates coordinates;
    private final String description;

    public Event(String title, GPSCoordinates coordinates, String description) {
        this.title = title;
        this.coordinates = coordinates;
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }

    public GPSCoordinates getGPSCoordinates() {
        return coordinates;
    }

}
