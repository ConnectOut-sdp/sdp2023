package com.sdpteam.connectout.event.nearbyEvents;

import java.util.ArrayList;
import java.util.List;

import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.EventRepository;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EventsViewModel extends ViewModel {

    private final EventRepository model;
    private final MutableLiveData<List<Event>> events;

    //TODO add event list filtering.

    public EventsViewModel(EventRepository model) {
        this.model = model;
        events = new MutableLiveData<>(new ArrayList<>());
        refreshEvents();
    }

    public LiveData<List<Event>> getEventListLiveData() {
        return events;
    }

    public void refreshEvents() {
        model.getEventsByFilter(null, null).thenAccept(events::setValue);
    }
}
