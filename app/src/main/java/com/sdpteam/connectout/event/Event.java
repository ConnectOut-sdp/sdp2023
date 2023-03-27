package com.sdpteam.connectout.event;

import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;

import java.util.ArrayList;
import java.util.List;

import com.sdpteam.connectout.map.GPSCoordinates;
import com.sdpteam.connectout.profile.ProfileID;

/**
 * This class describes an event
 * Every single marker on the map corresponds to a different one
 */
public class Event {

    public static Event NULL_EVENT = new Event();
    private final String id;
    private final String title;
    private final String description;
    private final GPSCoordinates coordinates;
    private final String organizer;
    private final List<ProfileID> participants = new ArrayList<>();

    private Event() {
        this(null, null, null, null, NULL_USER);
    }

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

    public GPSCoordinates getCoordinates() {
        return coordinates;
    }

    public String getOrganizer() {
        return organizer;
    }

    public List<ProfileID> getParticipants() {
        return participants;
    }
}
