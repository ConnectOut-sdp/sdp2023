package com.sdpteam.connectout.event.nearbyEvents.filter;

import java.util.List;
import java.util.function.Predicate;

import com.sdpteam.connectout.profile.Profile;

/**
 * Global interface filter for the participant's list.
 */
public interface ProfilesFilter extends Predicate<List<Profile>> {

    ProfilesFilter NONE = e -> true;

    @Override
    boolean test(List<Profile> profiles);

    default ProfilesFilter and(ProfilesFilter other) {
        return e -> test(e) && other.test(e);
    }

    default ProfilesFilter or(ProfilesFilter other) {
        return e -> test(e) || other.test(e);
    }

    default ProfilesFilter negate() {
        return e -> !test(e);
    }
}
