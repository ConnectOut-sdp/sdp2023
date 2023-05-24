package com.sdpteam.connectout.profile;

import java.util.List;

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
