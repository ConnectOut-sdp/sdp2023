package com.sdpteam.connectout.event;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class EventCreatorViewModel extends ViewModel {
    private final EventRepository eventRepository;
    private final MutableLiveData<Event> eventLiveData;

    public EventCreatorViewModel(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
        this.eventLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Event> getEventLiveData() {
        return eventLiveData;
    }

    /**
     * trigger a fetch
     */
    public void triggerFetchEvent(String eventId) {
        eventRepository.getEvent(eventId).thenAccept(eventLiveData::setValue);
    }

    /**
     * trigger a fetch
     */
    public void triggerFetchEvent(String userId, String title) {
        eventRepository.getEvent(userId, title).thenAccept(eventLiveData::setValue);
    }

    public void saveEvent(Event event) {
        eventRepository.saveEvent(event);
    }

    public String getUniqueId() {
        return eventRepository.getUniqueId();
    }
}
