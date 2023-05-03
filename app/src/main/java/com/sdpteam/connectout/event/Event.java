package com.sdpteam.connectout.event;

import static com.sdpteam.connectout.profile.EditProfileActivity.NULL_USER;

import com.sdpteam.connectout.event.nearbyEvents.map.GPSCoordinates;

import java.util.ArrayList;
import java.util.List;

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
    private final List<String> interestedParticipants;
    private final long date;

    private Event() {
        this(NULL_USER, "NullTitle", "NullDescription", new GPSCoordinates(0, 0), NULL_USER, new ArrayList<>(), new ArrayList<>(), 0);
    }

    public Event(String id, String title, String description, GPSCoordinates coordinates, String organizer) {
        this(id, title, description, coordinates, organizer, new ArrayList<>(), new ArrayList<>(), 0);
    }

    public Event(String id,
                 String title,
                 String description,
                 GPSCoordinates coordinates,
                 String organizer,
                 List<String> participants,
                 List<String> interestedParticipants) {
        this(id, title, description, coordinates, organizer, participants, interestedParticipants, 0);
    }

    public Event(String id,
                 String title,
                 String description,
                 GPSCoordinates coordinates,
                 String organizer,
                 List<String> participants,
                 List<String> interestedParticipants,
                 long date) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.coordinates = coordinates;
        this.organizer = organizer;
        this.date = date;
        this.participants = participants;
        this.interestedParticipants = interestedParticipants;
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

    public List<String> getInterestedParticipants() {
        return interestedParticipants;
    }

    public boolean hasJoined(String userId) {
        return participants.contains(userId);
    }

    public boolean isInterested(String userId) {
        return interestedParticipants.contains(userId);
    }

    public long getDate() {
        return date;
    }

    /**
     * Adds a participant to the list. Does nothing if already in the list. Removes it from the
     * interested participants list.
     *
     * @param id (String): id of the participant
     * @return (boolean): true if the participants was successfully added.
     */
    public boolean addParticipant(String id) {
        if (participants.contains(id)) {
            return false;
        }
        participants.add(id);
        interestedParticipants.remove(id);
        return true;
    }

    /**
     * Adds a participants to the interested list. Does nothing if already joined the event.
     *
     * @param id of the participant
     * @return true if the participants was successfully added to the interested list
     */
    public boolean addInterestedParticipant(String id) {
        if (participants.contains(id) || interestedParticipants.contains(id)) {
            return false;
        }
        interestedParticipants.add(id);
        return true;
    }

    /**
     * Completely removes the participant from list and interested list as well
     * @param id (String): id of the participant
     * @return (boolean): true if the participants was removed.
     */
    public boolean removeParticipant(String id) {
        interestedParticipants.remove(id);
        return participants.remove(id);
    }
}
