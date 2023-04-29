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

    /**A Profile must satisfy all these restrictions to be able to join an event*/
    public static class EventRestrictions {
        /**used to inform the user of the restriction that he does not satisfy when refused registration*/
        public enum RestrictionStatus{
            ALL_RESTRICTIONS_SATISFIED("Thank you for your registration"),
            INSUFFICIENT_RATING("Registration blocked due to insufficient rating"),
            MAX_NUM_PARTICIPANTS_REACHED("Registration blocked because the event is full");
            private String message;
            RestrictionStatus(String message){
                this.message = message;
            }
            public String getMessage(){return message;}
        }
        private final double minRating;

        private final int maxNumParticipants; //TODO add a time constraint

        public EventRestrictions(){
            this(-1, Integer.MAX_VALUE);
        }

        public EventRestrictions(double minRating, int maxNumParticipants){
            this.minRating = minRating;
            this.maxNumParticipants = maxNumParticipants;
        }

        public double getMinRating(){return minRating;}

        public int getMaxNumParticipants(){return maxNumParticipants;}
    }
    public static final Event NULL_EVENT = new Event();

    private final String id;
    private final String title;
    private final String description;
    private final GPSCoordinates coordinates;
    private final String organizer;
    private final List<String> participants;
    private final long date;
    private final EventRestrictions restrictions;

    private Event() {
        this(NULL_USER, "NullTitle", "NullDescription", new GPSCoordinates(0,0), NULL_USER, new ArrayList<>(), 0);
    }

    public Event(String id, String title, String description, GPSCoordinates coordinates, String organizer) {
        this(id, title, description, coordinates, organizer,new ArrayList<>(), 0);
    }


    public Event(String id, String title, String description, GPSCoordinates coordinates, String organizer, List<String> participants) {
        this(id, title, description, coordinates, organizer,participants, 0);
    }

    public Event(String id, String title, String description, GPSCoordinates coordinates, String organizer,List<String> participants, long date) {
        this(id, title, description, coordinates, organizer, participants, date, new EventRestrictions());
    }
    public Event(String id, String title, String description, GPSCoordinates coordinates, String organizer,List<String> participants, long date, EventRestrictions restrictions) {
        this.id = id;
        this.title = title;
        this.description = description;
        this.coordinates = coordinates;
        this.organizer = organizer;
        this.date = date;
        this.participants = participants;
        this.restrictions = restrictions;
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

    public EventRestrictions getRestrictions(){return restrictions;}
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
