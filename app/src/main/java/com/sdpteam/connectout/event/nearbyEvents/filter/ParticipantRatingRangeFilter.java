package com.sdpteam.connectout.event.nearbyEvents.filter;

import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.profile.Profile;

import java.util.List;

public class ParticipantRatingRangeFilter implements ParticipantsFilter{

    private final Double val1;
    private final Double val2;

    public ParticipantRatingRangeFilter(Double val1, Double val2){
        this.val1 = Math.min(val1,val2);
        this.val2 = Math.max(val1,val2);
    }

    @Override
    public boolean test(List<Profile> profiles) {
        return profiles.stream().map(Profile::getRating).allMatch(value -> val1 <= value &&  value <=val2);
    }
}
