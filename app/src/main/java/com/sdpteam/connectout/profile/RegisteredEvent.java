package com.sdpteam.connectout.profile;

/**
 * This class represents the child Users/RegisteredEvents in the Firebase structure.
 * It is used to build back the {@link ProfileEntry} when we fetch the profiles.
 */
public class RegisteredEvent {
    private final long eventDate;
    private final String eventId;
    private final String eventTitle;
    public static final RegisteredEvent NULL_REGISTERED_EVENT = new RegisteredEvent();

    private RegisteredEvent() {
        this(0, "", "");
    }

    public RegisteredEvent(long eventDate, String eventId, String eventTitle) {
        this.eventDate = eventDate;
        this.eventId = eventId;
        this.eventTitle = eventTitle;
    }

    public long getEventDate() {
        return eventDate;
    }

    public String getEventId() {
        return eventId;
    }

    public String getEventTitle() {
        return eventTitle;
    }
}
