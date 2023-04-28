package com.sdpteam.connectout.event.viewer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.EventRepository;

public class EventViewModel extends ViewModel {

    private final EventRepository eventRepository;
    private final MutableLiveData<Event> eventLiveData;
    private String lastEventId;

    public EventViewModel(EventRepository eventRepository) {
        this.eventRepository = eventRepository;
        eventLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Event> getEventLiveData() {
        return eventLiveData;
    }

    public void getEvent(String eventId) {
        lastEventId = eventId;
        eventRepository.getEvent(eventId).thenAccept(eventLiveData::setValue);
    }

    /**
     * Fetches the event with the last stored eventId and updates the eventLiveData accordingly.
     */
    public void refreshEvent() {
        eventRepository.getEvent(lastEventId).thenAccept(eventLiveData::setValue);
    }

    /**
     * Updates the participation status of the specified user in the event to "attending".
     *
     * @param userId (String): id of the user to add to the event
     * @return (LiveData < Boolean >): true if participation status has been joined.
     */
    public LiveData<Boolean> joinEvent(String userId) {
        return updateParticipationStatus(userId, true);
    }

    /**
     * Updates the participation status of the specified user in the event to "not attending".
     *
     * @param userId (String): id of the user to remove from the event
     * @return (LiveData < Boolean >): true if participation status has been left.
     */
    public LiveData<Boolean> leaveEvent(String userId) {
        return updateParticipationStatus(userId, false);
    }

    /**
     * Toggles the participation status of the specified user in the event,
     * if the user is attending/left the event, it removes/add the user from the event.
     *
     * @param userId (String): id of the user whose participation status needs to be toggled
     * @return (LiveData < Boolean >): upon update, true if participation status has been changed.
     */
    public LiveData<Boolean> toggleParticipation(String userId) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        eventRepository.getEvent(lastEventId).thenAccept(event ->
                updateParticipationStatus(userId, !event.getParticipants().contains(userId)).observeForever(result::setValue));
        return result;
    }

    /**
     * Updates the participation status of the specified user in the event, and refreshes the eventLiveData with the updated event.
     *
     * @param userId          (String): id of the user whose participation status needs to be updated
     * @param isParticipating (boolean): true if the user is attending the event, false otherwise
     * @return (LiveData < Boolean >): upon update, true if participation status has been changed.
     */
    private LiveData<Boolean> updateParticipationStatus(String userId, boolean isParticipating) {
        MutableLiveData<Boolean> result = new MutableLiveData<>();
        // Check if an event is selected
        if (lastEventId != null) {
            if (isParticipating) {
                eventRepository.joinEvent(lastEventId, userId).thenAccept(b ->
                        {
                            refreshEvent();
                            result.setValue(b);
                        }
                );
            } else {
                eventRepository.leaveEvent(lastEventId, userId).thenAccept(b -> {
                    refreshEvent();
                    result.setValue(b);
                });
            }
        }
        return result;
    }
}
