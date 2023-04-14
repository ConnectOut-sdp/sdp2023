package com.sdpteam.connectout.event.nearbyEvents;

import java.util.ArrayList;
import java.util.List;

import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.EventRepository;
import com.sdpteam.connectout.event.nearbyEvents.filter.EventFilter;
import com.sdpteam.connectout.event.nearbyEvents.filter.ProfilesFilter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EventsViewModel extends ViewModel {

    private final EventRepository model;
    private final MutableLiveData<List<Event>> events = new MutableLiveData<>(new ArrayList<>());
    private EventFilter eventFilter = EventFilter.NONE;
    private ProfilesFilter profilesFilter = ProfilesFilter.NONE;

    public EventsViewModel(EventRepository model) {
        this.model = model;
        refreshEvents();
    }

    public LiveData<List<Event>> getEventListLiveData() {
        return events;
    }

    public void setFilter(EventFilter eventFilter, ProfilesFilter profilesFilter) {
        this.profilesFilter = profilesFilter;
        this.eventFilter = eventFilter;
    }

    public void refreshEvents() {
        model.getEventsByFilter(eventFilter, profilesFilter).thenAccept(events::setValue);
    }
}
