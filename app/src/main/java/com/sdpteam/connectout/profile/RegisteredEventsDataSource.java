package com.sdpteam.connectout.profile;


public interface RegisteredEventsDataSource {

    /**
     * stores a new Profile.CalendarEvent (eventId, eventTitle and eventDate)
     * in list of events that a profile is registered to
     */
    void registerToEvent(RegisteredEvent event, String profileId);


}
