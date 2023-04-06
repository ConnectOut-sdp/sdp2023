package com.sdpteam.connectout.event.nearbyEvents.filter;

import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.profile.Profile;

import java.util.List;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public class BinaryFilter {
    private  EventFilter eventFilter;
    private  ParticipantsFilter participantsFilter;

    public final static BinaryFilter NONE = new BinaryFilter();

    public BinaryFilter(){
        eventFilter = EventFilter.NONE;
        participantsFilter = ParticipantsFilter.NONE;
    }
    public BinaryFilter(EventFilter eventFilter){
        this.eventFilter = eventFilter;
        participantsFilter = ParticipantsFilter.NONE;
    }

    public boolean testEvent(Event event){
        return eventFilter.test(event);
    }
    public boolean testParticipants(List<Profile> profiles){
        return participantsFilter.test(profiles);
    }
    public BinaryFilter and(EventFilter eventFilter, ParticipantsFilter participantsFilter) {
        this.eventFilter = this.eventFilter.and(eventFilter);
        this.participantsFilter = this.participantsFilter.and(participantsFilter);
        return this;
    }
    public BinaryFilter or(EventFilter eventFilter, ParticipantsFilter participantsFilter) {
        this.eventFilter = this.eventFilter.or(eventFilter);
        this.participantsFilter = this.participantsFilter.or(participantsFilter);
        return this;
    }
    public BinaryFilter negate() {
        this.eventFilter = this.eventFilter.negate();
        this.participantsFilter = this.participantsFilter.negate();
        return this;
    }
}
