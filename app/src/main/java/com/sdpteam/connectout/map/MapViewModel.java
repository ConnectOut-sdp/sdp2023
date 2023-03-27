package com.sdpteam.connectout.map;

import java.util.List;

import com.sdpteam.connectout.event.Event;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MapViewModel extends ViewModel {

    private final MapModelManager model;
    private final MutableLiveData<List<Event>> events;

    public MapViewModel(MapModelManager model) {
        this.model = model;
        this.events = model.getEventLiveList();
    }

    public LiveData<List<Event>> getEventListLiveData() {
        return events;
    }

    public void triggerRefreshEventList() {
        events.postValue(model.getEventLiveList().getValue());
    }
}
