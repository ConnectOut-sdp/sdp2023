package com.sdpteam.connectout.event;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.EventRepository;

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

    public LiveData<List<Event>> getEventList() {
        return events;
    }

    public LiveData<List<Event>> refreshEventList() {
        model.getEventLiveList(null, null).thenAccept(events::setValue);
        return events;
    }
}
