package com.sdpteam.connectout.event.nearbyEvents;

import java.util.ArrayList;
import java.util.List;

import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.EventDataSource;
import com.sdpteam.connectout.event.nearbyEvents.filter.EventFilter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EventsViewModel extends ViewModel {

    private final EventDataSource model;
    private final MutableLiveData<List<Event>> events = new MutableLiveData<>(new ArrayList<>());
    private EventFilter eventFilter;

    public EventsViewModel(EventDataSource model) {
        this(model, EventFilter.NONE);
    }

    public EventsViewModel(EventDataSource model, EventFilter eventFilter) {
        this.model = model;
        this.eventFilter = eventFilter;
        refreshEvents();
    }

    public LiveData<List<Event>> getEventListLiveData() {
        return events;
    }

    public void setFilter(EventFilter eventFilter) {
        this.eventFilter = eventFilter;
    }

    public void refreshEvents() {
        model.getEventsByFilter(eventFilter).thenAccept(events::setValue);
    }
}
