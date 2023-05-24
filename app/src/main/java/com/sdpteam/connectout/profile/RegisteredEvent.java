package com.sdpteam.connectout.profile;

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
