package com.sdpteam.connectout.event;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class EventCreatorViewModel extends ViewModel {
    private final EventDataManager model;


    public EventCreatorViewModel(EventDataManager model) {
        this.model = model;
    }


    public LiveData<Event> getValue(String eventId) {
        return model.getValue(eventId);
    }

    public LiveData<Event> getValue(String userId, String title) {
        return model.getValue(userId, title);
    }

    public void saveValue(Event event) {
         model.saveValue(event);
    }

    public  String getUniqueId(){
        return model.getUniqueId();
    }

}
