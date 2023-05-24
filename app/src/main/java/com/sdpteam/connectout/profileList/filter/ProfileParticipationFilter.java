package com.sdpteam.connectout.profileList.filter;

import com.sdpteam.connectout.profile.ProfileEntry;

public class ProfileParticipationFilter implements ProfileFilter {

    public final String eventId;

    public ProfileParticipationFilter(String eventId) {
        this.eventId = eventId;
    }

    @Override
    public boolean test(ProfileEntry entry) {
        return entry.getRegisteredEvents().stream().anyMatch(registeredEvent -> registeredEvent.getEventId().equals(eventId));
    }
}
