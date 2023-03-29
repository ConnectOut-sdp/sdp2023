package com.sdpteam.connectout.event;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import java.util.ArrayList;
import java.util.List;

public class EventsViewModel extends ViewModel {

    private final EventRepository model;
    private final MutableLiveData<List<Event>> events;

    //TODO add event list filtering.

    public EventsViewModel(EventRepository model) {
        this.model = model;
        events = new MutableLiveData<>(new ArrayList<>());
        refreshEventList();
    }

    public LiveData<List<Event>> getEventListLiveData() {
        return events;
    }

    public void refreshEventList() {
        model.getEventsByFilter(null, null).thenAccept(events::setValue);
    }
}
