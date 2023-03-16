package com.sdpteam.connectout.event;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

public class EventCreatorViewModel extends ViewModel {
    private final EventDataManager model;


    public EventCreatorViewModel(EventDataManager model) {
        this.model = model;
    }


    public LiveData<Event> getEvent(String eventId) {
        return model.getEvent(eventId);
    }

    public LiveData<Event> getEvent(String userId, String title) {
        return model.getEvent(userId, title);
    }

    public void saveEvent(Event event) {
         model.saveEvent(event);
    }

    public  String getUniqueId(){
        return model.getUniqueId();
    }

}
