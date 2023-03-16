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

    private final String id;
    private final String title;
    private final String description;
    private final GPSCoordinates coordinates;
    private final ProfileID organizer;
    private final Set<ProfileID> participants;

    public Event(String title, GPSCoordinates coordinates, String description) {
        this(null, title, description, coordinates, null, new HashSet<>());
    }

    public Event(String id,
                 String title,
                 String description,
                 GPSCoordinates coordinates,
                 ProfileID organizer,
                 Set<ProfileID> participants) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.coordinates = coordinates;
        this.organizer = organizer;
        this.participants = participants;
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

    public ProfileID getOrganizer() {
        return organizer;
    }

    public Set<ProfileID> getParticipants() {
        return participants;
    }
}
