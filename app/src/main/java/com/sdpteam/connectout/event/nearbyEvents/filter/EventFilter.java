package com.sdpteam.connectout.event.nearbyEvents.filter;

import com.sdpteam.connectout.event.Event;

import java.util.function.Predicate;

public interface EventFilter extends Predicate<Event> {

    EventFilter NONE = e -> true;

    @Override
    boolean test(Event event);

    default EventFilter and(EventFilter other) {
        return e -> test(e) && other.test(e);
    }
}
