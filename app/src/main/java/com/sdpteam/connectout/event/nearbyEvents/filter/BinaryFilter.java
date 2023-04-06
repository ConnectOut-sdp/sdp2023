package com.sdpteam.connectout.event.nearbyEvents.filter;

import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.profile.Profile;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class BinaryFilter {
    private final EventFilter eventFilter;
    private final ParticipantsFilter participantsFilter;

    public final static BinaryFilter NONE = new BinaryFilter();

    public BinaryFilter(){
        eventFilter = EventFilter.NONE;
        participantsFilter = ParticipantsFilter.NONE;
    }
    public BinaryFilter(EventFilter eventFilter){
        this.eventFilter = eventFilter;
        participantsFilter = ParticipantsFilter.NONE;
    }
    public BinaryFilter(EventFilter eventFilter, ParticipantsFilter participantsFilter){
        this.eventFilter = eventFilter;
        this.participantsFilter = participantsFilter;
    }

    public boolean testEvent(Event event){
        return eventFilter.test(event);
    }
    public boolean testParticipants(List<Profile> profiles){
        return participantsFilter.test(profiles);
    }
    public BinaryFilter and(EventFilter eventFilter, ParticipantsFilter participantsFilter) {
        return new BinaryFilter(this.eventFilter.and(eventFilter), this.participantsFilter.and(participantsFilter));
    }
    public BinaryFilter or(EventFilter eventFilter, ParticipantsFilter participantsFilter) {
        return new BinaryFilter(this.eventFilter.or(eventFilter), this.participantsFilter.or(participantsFilter));

    }
    public BinaryFilter negate() {
        return new BinaryFilter(this.eventFilter.negate(), this.participantsFilter.negate());
    }
}
