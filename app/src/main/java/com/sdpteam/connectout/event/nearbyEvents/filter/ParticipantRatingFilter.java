package com.sdpteam.connectout.event.nearbyEvents.filter;

import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.profile.Profile;

import java.util.List;

public class ParticipantRatingFilter implements BinaryFilter {

    private final Double val;

    public ParticipantRatingFilter(Double value){

        this.val = value;
    }
    @Override
    public boolean test(Event event, List<Profile> profiles) {
        return profiles.stream().map(Profile::getRating).allMatch(value -> val <= value);
    }
}
