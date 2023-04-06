package com.sdpteam.connectout.event.nearbyEvents.filter;

import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.profile.Profile;

import java.util.List;
import java.util.function.BiPredicate;

public interface BinaryFilter extends BiPredicate<Event, List<Profile>> {
    BinaryFilter NONE = (e,p) -> true;

    @Override
    boolean test(Event event, List<Profile> profiles);

    default BinaryFilter and(BinaryFilter other) {
        return (e,p) -> test(e,p) && other.test(e,p);
    }
    default BinaryFilter or(BinaryFilter other) {
        return (e,p) -> test(e,p) || other.test(e,p);
    }
    default BinaryFilter negate() {
        return (e,p) -> !test(e,p);
    }
}
