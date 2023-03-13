package com.sdpteam.connectout.event;

import com.sdpteam.connectout.map.GPSCoordinates;
import com.sdpteam.connectout.profile.EditProfileActivity;

/**
 * This class describes an event
 * Every single marker on the map corresponds to a different one
 */
public class Event {

    private final String title;
    private final GPSCoordinates gpsCoordinates;
    private final String description;
    private final String ownerId;
    private final String eventId;

    public final static Event NULL_EVENT = new Event();

    private Event() {
        this(null, null, null, EditProfileActivity.NULL_USER, null);
    }

    public Event(String title, GPSCoordinates gpsCoordinates, String description, String ownerId, String eventId) {
        this.title = title;
        this.gpsCoordinates = gpsCoordinates;
        this.description = description;
        this.ownerId = ownerId;
        this.eventId = eventId;
    }

    public String getDescription() {
        return description;
    }

    public String getTitle() {
        return title;
    }


    public GPSCoordinates getGpsCoordinates() {
        return gpsCoordinates;
    }

    public String getOwnerId() {
        return ownerId;
    }

    public String getEventId() {
        return eventId;
    }
}
