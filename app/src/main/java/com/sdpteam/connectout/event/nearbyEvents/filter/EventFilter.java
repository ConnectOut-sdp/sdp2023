package com.sdpteam.connectout.event.nearbyEvents.filter;

import java.util.function.Predicate;

import com.sdpteam.connectout.event.Event;

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
