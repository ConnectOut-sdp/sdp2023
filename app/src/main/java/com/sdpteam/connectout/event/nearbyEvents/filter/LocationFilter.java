package com.sdpteam.connectout.event.nearbyEvents.filter;

import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.nearbyEvents.map.GPSCoordinates;

public class LocationFilter implements EventFilter {

    private final GPSCoordinates position;
    private final int radius; // in km

    public LocationFilter(GPSCoordinates position, int radius) {
        this.position = position;
        this.radius = radius;
    }

    @Override
    public boolean test(Event event) {
        if (position == null || event.getCoordinates() == null) {
            return false;
        }
        if (radius == 100) { // seek bar at max value behaves like disabled filter
            return true;
        }
        return event.getCoordinates().distanceTo(position) <= radius;
    }
}
