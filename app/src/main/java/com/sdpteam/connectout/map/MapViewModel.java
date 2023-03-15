package com.sdpteam.connectout.map;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sdpteam.connectout.event.Event;

import java.util.List;

public class MapViewModel extends ViewModel {

    private final MapModel model;
    private final MutableLiveData<List<Event>> events;

    public MapViewModel(MapModel model) {
        this.model = model;
        this.events = model.getEventLiveList();
    }

    public LiveData<List<Event>> getEventList() {
        return events;
    }

    public LiveData<List<Event>> refreshEventList() {
        events.postValue(model.getEventLiveList().getValue());
        return events;
    }
}
