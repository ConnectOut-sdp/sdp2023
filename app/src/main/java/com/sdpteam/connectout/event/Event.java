package com.sdpteam.connectout.event;

import com.sdpteam.connectout.map.GPSCoordinates;
import com.sdpteam.connectout.profile.ProfileID;

import java.util.HashSet;
import java.util.Set;

/**
 * This class describes an event
 * Every single marker on the map corresponds to a different one
 */
public class Event {

    public static Event NULL_EVENT = new Event(null, null, null, null, null);
    private final String id;
    private final String title;
    private final String description;
    private final GPSCoordinates coordinates;
    private final String organizer;
    private final Set<ProfileID> participants = new HashSet<>();

    public Event(String id, String title, String description, GPSCoordinates coordinates, String organizer) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.coordinates = coordinates;
        this.organizer = organizer;
    }

    public String getId() {
        return id;
    }

    public String getTitle() {
        return title;
    }

    public String getDescription() {
        return description;
    }

    public GPSCoordinates getGPSCoordinates() {
        return coordinates;
    }

    public String getOrganizer() {
        return organizer;
    }

    public Set<ProfileID> getParticipants() {
        return participants;
    }
}
