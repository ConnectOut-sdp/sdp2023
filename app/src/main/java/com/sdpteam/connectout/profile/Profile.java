package com.sdpteam.connectout.profile;

import java.util.ArrayList;

public class Profile {

    public class CalendarEvent {
        private String eventId;
        private String eventTitle;
        private final long eventDate;

        public CalendarEvent(String eventId, String eventTitle, long eventDate){
            this.eventId = eventId;
            this.eventTitle = eventTitle;
            this.eventDate = eventDate;
        }
        public String getEventTitle() {
            return eventTitle;
        }
        public long getEventDate(){
            return eventDate;
        }
        public String getEventId(){
            return eventId;
        }
    }
    public final static Profile NULL_PROFILE = new Profile();
    private final String name;
    private final String email;
    private final String bio;
    private final Gender gender;

    private final double rating;

    private final int numRatings;

    private final String uid;

    private final ArrayList<CalendarEvent> registeredEvents;

    private Profile() {
        this(EditProfileActivity.NULL_USER, EditProfileActivity.NULL_USER, "NULL_EMAIL", "NULL_BIO", Gender.OTHER, 0, 0, new ArrayList<>());
    }

    public Profile(String uid, String name, String email, String bio, Gender gender, double rating, int numRatings) {
        this(uid, name, email, bio, gender, rating, numRatings, new ArrayList<>());
    }
    public Profile(String uid, String name, String email, String bio, Gender gender, double rating, int numRatings, ArrayList<CalendarEvent> registeredEvents) {
        this.uid = uid;
        this.name = name;
        this.email = email;
        this.bio = bio;
        this.gender = gender;
        this.rating = rating;
        this.numRatings = numRatings;
        this.registeredEvents = registeredEvents;
    }


    public Gender getGender() {
        return gender;
    }

    public String getBio() {
        return bio;
    }

    public String getEmail() {
        return email;
    }

    public String getName() {
        return name;
    }

    public String getId() {
        return uid;
    }

    public double getRating() {
        return rating;
    }

    public int getNumRatings() {
        return numRatings;
    }

    public enum Gender {
        FEMALE, MALE, OTHER
    }

    public ArrayList<CalendarEvent> getRegisteredEvents(){return registeredEvents;}
}
