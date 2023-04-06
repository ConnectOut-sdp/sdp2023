package com.sdpteam.connectout.event.nearbyEvents.filter;

import com.sdpteam.connectout.profile.Profile;

import java.util.List;

/**
 * Filters participants by their ratings
 */
public class ParticipantRatingFilter implements ParticipantsFilter {

    private final Double val;

    public ParticipantRatingFilter(Double value){

        this.val = value;
    }
    @Override
    public boolean test(List<Profile> profiles) {
        if(val == null){
            return true;
        }
        return profiles.stream().map(Profile::getRating).allMatch(value -> val <= value);
    }
}
