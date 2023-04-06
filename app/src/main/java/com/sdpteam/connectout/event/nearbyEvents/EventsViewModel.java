package com.sdpteam.connectout.event.nearbyEvents;

import java.util.ArrayList;
import java.util.List;

import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.EventRepository;
import com.sdpteam.connectout.event.nearbyEvents.filter.BinaryFilter;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EventsViewModel extends ViewModel {

    private final EventRepository model;
    private final MutableLiveData<List<Event>> events = new MutableLiveData<>(new ArrayList<>());
    private BinaryFilter filter = BinaryFilter.NONE;

    public EventsViewModel(EventRepository model) {
        this.model = model;
        refreshEvents();
    }

    public LiveData<List<Event>> getEventListLiveData() {
        return events;
    }

    public void setFilter(BinaryFilter filter) {
        this.filter = filter;
    }

    public void refreshEvents() {
        model.getEventsByFilter(filter).thenAccept(events::setValue);
    }
}
