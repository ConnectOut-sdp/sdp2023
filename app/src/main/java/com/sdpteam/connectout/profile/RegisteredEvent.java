package com.sdpteam.connectout.profile;

/**
 * This class represents the child Users/RegisteredEvents in the Firebase structure.
 * It is used to build back the {@link ProfileEntry} when we fetch the profiles.
 */
public class RegisteredEvent {
    public static final RegisteredEvent NULL_REGISTERED_EVENT = new RegisteredEvent();
    private final String eventId;

    private RegisteredEvent() {
        this("null_event");
    }

    public RegisteredEvent(String eventId) {
        this.eventId = eventId;
    }

    public String getEventId() {
        return eventId;
    }
}
