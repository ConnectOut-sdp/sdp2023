package com.sdpteam.connectout.event.nearbyEvents.filter;

import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.profile.Profile;

import java.util.List;
import java.util.function.Predicate;

public interface EventFilter extends Predicate<Event> {

    EventFilter NONE = e -> true;

    @Override
    boolean test(Event event);

    default EventFilter and(EventFilter other) {
        return e -> test(e) && other.test(e);
    }
    default EventFilter or(EventFilter other) {
        return e -> test(e) || other.test(e);
    }
    default EventFilter negate() {
        return e -> !test(e);
    }
}
