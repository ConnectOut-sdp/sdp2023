package com.sdpteam.connectout.event;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class EventCreatorViewModel extends ViewModel {
    private final EventDataManager model;


    public EventCreatorViewModel(EventDataManager model) {
        this.model = model;
    }


    public LiveData<Event> getValue(String eid) {
        return model.getValue(eid);
    }

    public LiveData<Event> getValue(String uid, String title) {
        return model.getValue(uid, title);
    }

    public boolean saveValue(Event event) {
        return model.saveValue(event);
    }
}
