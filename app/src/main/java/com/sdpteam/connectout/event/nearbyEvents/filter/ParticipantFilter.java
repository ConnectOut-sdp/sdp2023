package com.sdpteam.connectout.event.nearbyEvents.filter;

import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.profile.Profile;
import java.util.List;

public class ParticipantFilter implements BinaryFilter{

    private final String name;

    ParticipantFilter(String name){

        this.name = name;
    }
    @Override
    public boolean test(Event event, List<Profile> profiles) {
        return profiles.stream().map(Profile::getName).anyMatch(name::equals);
    }
}
