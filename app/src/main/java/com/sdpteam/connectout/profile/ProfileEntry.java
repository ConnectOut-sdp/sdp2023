package com.sdpteam.connectout.profile;

import java.util.List;

/**
 * A profile entry represents one entry of the users collection in the Firebase.
 * This class basically reflects the current structure in Firebase. It is used for profiles
 * filtering as we can filter by the attributes of a profile and its participation
 * to some specific events as well.
 */
public class ProfileEntry {

    private final Profile profile;
    private final List<RegisteredEvent> registeredEvents;

    public ProfileEntry(Profile profile, List<RegisteredEvent> registeredEvents) {
        this.profile = profile;
        this.registeredEvents = registeredEvents;
    }

    public Profile getProfile() {
        return profile;
    }

    public List<RegisteredEvent> getRegisteredEvents() {
        return registeredEvents;
    }
}
