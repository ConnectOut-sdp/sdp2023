package com.sdpteam.connectout.event.creator;

import androidx.lifecycle.MutableLiveData;

import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.EventDataSource;
import com.sdpteam.connectout.event.nearbyEvents.EventsViewModel;

public class EventCreatorViewModel extends EventsViewModel {
    private final EventDataSource model;
    private final MutableLiveData<Event> eventLiveData;

    public EventCreatorViewModel(EventDataSource model) {
        super(model);
        this.model = model;
        this.eventLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Event> getEventLiveData() {
        return eventLiveData;
    }

    public void getEvent(String eventId) {
        model.getEvent(eventId).thenAccept(eventLiveData::setValue);
    }

    public void getEvent(String userId, String title) {
        model.getEvent(userId, title).thenAccept(eventLiveData::setValue);
    }

    public Boolean saveEvent(Event event) {
        return model.saveEvent(event);
    }

    public String getUniqueId() {
        return model.getUniqueId();
    }
}
