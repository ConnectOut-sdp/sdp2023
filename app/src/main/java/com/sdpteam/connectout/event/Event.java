package com.sdpteam.connectout.event;

import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import com.sdpteam.connectout.event.nearbyEvents.map.GPSCoordinates;

/**
 * This class describes an event
 * Every single marker on the map corresponds to a different one
 */
public class Event {
    public static final Event NULL_EVENT = new Event();

    private final String id;
    private final String title;
    private final String description;
    private final GPSCoordinates coordinates;
    private final String organizer;
    private final List<String> participants;

    private Event() {
        this(null, null, null, null, NULL_USER);
    }

    public Event(String id, String title, String description, GPSCoordinates coordinates, String organizer, List<String> participants) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.coordinates = coordinates;
        this.organizer = organizer;
        this.participants = participants;

    }

    public Event(String id, String title, String description, GPSCoordinates coordinates, String organizer) {
        this(id,title,description,coordinates,organizer, new ArrayList<>());
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

    /**
     *
     * @param id (String): id of the participant
     * @return (boolean): true if the participants was added.
     */
    public boolean addParticipant(String id) {
        boolean absent = !participants.contains(id);
        if(absent){
            participants.add(id);
        }
        return absent;
    }
    /**
     *
     * @param id (String): id of the participant
     * @return (boolean): true if the participants was removed.
     */
    public boolean removeParticipant(String id) {
        return participants.remove(id);
    }
}
