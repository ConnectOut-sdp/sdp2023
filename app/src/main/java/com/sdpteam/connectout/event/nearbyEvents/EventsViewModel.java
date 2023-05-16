package com.sdpteam.connectout.event.nearbyEvents;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.EventDataSource;
import com.sdpteam.connectout.event.nearbyEvents.filter.EventFilter;
import com.sdpteam.connectout.event.nearbyEvents.filter.ProfilesFilter;

import java.util.ArrayList;
import java.util.List;

public class EventsViewModel extends ViewModel {

    private final EventDataSource model;
    private final MutableLiveData<List<Event>> events = new MutableLiveData<>(new ArrayList<>());
    private EventFilter eventFilter = EventFilter.NONE;
    private ProfilesFilter profilesFilter = ProfilesFilter.NONE;

    public EventsViewModel(EventDataSource model) {
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
