package com.sdpteam.connectout.event;

import androidx.lifecycle.MutableLiveData;

public class EventCreatorViewModel extends EventsViewModel {
    private final EventRepository model;
    private final MutableLiveData<Event> eventLiveData;

    public EventCreatorViewModel(EventRepository model) {
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

    public void saveEvent(Event event) {
        model.saveEvent(event);
    }

    public String getUniqueId() {
        return model.getUniqueId();
    }
}
