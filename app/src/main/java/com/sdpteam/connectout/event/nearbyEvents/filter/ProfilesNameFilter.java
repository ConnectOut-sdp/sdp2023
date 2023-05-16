package com.sdpteam.connectout.event.nearbyEvents.filter;

import com.sdpteam.connectout.profile.Profile;

import java.util.List;

/**
 * Filters participants by their name.
 */
public class ProfilesNameFilter implements ProfilesFilter {

    private final String name;

    public ProfilesNameFilter(String name) {

        this.name = name;
    }

    @Override
    public boolean test(List<Profile> profiles) {
        return profiles.stream().map(Profile::getName).anyMatch(name::equals);
    }
}
