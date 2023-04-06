package com.sdpteam.connectout.event.nearbyEvents.filter;

import com.sdpteam.connectout.event.Event;

public class ParticipantIdFilter implements EventFilter {

    private final String userId;

    public ParticipantIdFilter(String userId){

        this.userId = userId;
    }
    @Override
    public boolean test(Event event) {
        if(userId == null){
            return false;
        }
        return userId.equals(event.getOrganizer()) || event.getParticipants().contains(userId);
    }
}
