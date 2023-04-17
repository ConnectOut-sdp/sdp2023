package com.sdpteam.connectout.event;

import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;

import java.util.ArrayList;
import java.util.List;

import com.sdpteam.connectout.event.nearbyEvents.map.GPSCoordinates;

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
    private final List<String> participants;

    private final long date;

    private Event() {
        this(NULL_USER, "NullTitle", "NullDescription", new GPSCoordinates(0,0), NULL_USER, new ArrayList<>(), 0);
    }

    public Event(String id, String title, String description, GPSCoordinates coordinates, String organizer) {
        this(id, title, description, coordinates, organizer,new ArrayList<>(), 0);
    }

    public Event(String id, String title, String description, GPSCoordinates coordinates, String organizer,List<String> participants, long date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.coordinates = coordinates;
        this.organizer = organizer;
        this.participants = participants;
        this.date = date;
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

    public List<String> getParticipants() {
        return participants;
    }

    public long getDate(){return date;}
}
