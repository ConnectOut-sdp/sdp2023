package com.sdpteam.connectout.event.nearbyEvents.filter;

import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.profile.Profile;
import java.util.List;

public class ParticipantNameFilter implements ParticipantsFilter{

    private final String name;

    public ParticipantNameFilter(String name){

        this.name = name;
    }
    @Override
    public boolean test(List<Profile> profiles) {
        return profiles.stream().map(Profile::getName).anyMatch(name::equals);
    }
}