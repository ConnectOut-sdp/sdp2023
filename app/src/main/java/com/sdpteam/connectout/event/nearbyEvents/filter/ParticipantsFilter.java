package com.sdpteam.connectout.event.nearbyEvents.filter;

import com.sdpteam.connectout.profile.Profile;

import java.util.List;
import java.util.function.Predicate;

/**
 * Global interface filter for the participant's list.
 */
public interface ParticipantsFilter extends Predicate<List<Profile>> {

    ParticipantsFilter NONE = e -> true;
    @Override
    boolean test(List<Profile> profiles);

    default ParticipantsFilter and(ParticipantsFilter other) {
        return e -> test(e) && other.test(e);
    }
    default ParticipantsFilter or(ParticipantsFilter other) {
        return e -> test(e) || other.test(e);
    }
    default ParticipantsFilter negate() {
        return e -> !test(e);
    }
}
