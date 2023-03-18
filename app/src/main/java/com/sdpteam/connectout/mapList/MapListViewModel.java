package com.sdpteam.connectout.mapList;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sdpteam.connectout.event.Event;

import java.util.List;

public class MapListViewModel extends ViewModel {

    private final MapListModelManager model;
    private MutableLiveData<List<Event>> events;

    //TODO add event list filtering.
    public MapListViewModel(MapListModelManager model) {
        this.model = model;
        this.events = model.getEventLiveList(null, null);
    }

    public LiveData<List<Event>> getEventList() {
        return events;
    }

    public LiveData<List<Event>> refreshEventList() {
        events = model.getEventLiveList(null, null);
        return events;
    }
}
