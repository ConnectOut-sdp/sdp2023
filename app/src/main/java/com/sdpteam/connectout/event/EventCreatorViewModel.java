package com.sdpteam.connectout.event;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModel;

import com.sdpteam.connectout.mapList.MapListModel;
import com.sdpteam.connectout.mapList.MapListModelManager;
import com.sdpteam.connectout.mapList.MapListViewModel;

public class EventCreatorViewModel extends MapListViewModel {
    private final EventDataManager model;


    public EventCreatorViewModel(EventDataManager model) {
        super((MapListModelManager) model);
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
