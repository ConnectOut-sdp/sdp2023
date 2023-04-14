package com.sdpteam.connectout.event.nearbyEvents.filter;

import com.sdpteam.connectout.profile.Profile;

import java.util.List;

/**
 * Filters participants by a rating range
 */
public class ProfilesRatingRangeFilter implements ProfilesFilter {

    private final Double val1;
    private final Double val2;

    public ProfilesRatingRangeFilter(Double val1, Double val2){
        this.val1 = Math.min(val1,val2);
        this.val2 = Math.max(val1,val2);
    }

    @Override
    public boolean test(List<Profile> profiles) {
        return profiles.stream().map(Profile::getRating).allMatch(value -> val1 <= value &&  value <=val2);
    }
}
