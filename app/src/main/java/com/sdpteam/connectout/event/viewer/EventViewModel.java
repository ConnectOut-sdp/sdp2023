package com.sdpteam.connectout.event.viewer;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.EventDataSource;
import com.sdpteam.connectout.notifications.EventNotificationManager;

import java.util.concurrent.CompletableFuture;

public class EventViewModel extends ViewModel {

    private final EventDataSource eventDataSource;
    private final MutableLiveData<Event> eventLiveData;
    private String lastEventId;

    public EventViewModel(EventDataSource eventDataSource) {
        this.eventDataSource = eventDataSource;
        eventLiveData = new MutableLiveData<>();
    }

    public MutableLiveData<Event> getEventLiveData() {
        return eventLiveData;
    }

    public void getEvent(String eventId) {
        lastEventId = eventId;
        eventDataSource.getEvent(eventId).thenAccept(eventLiveData::setValue);
    }

    /**
     * Fetches the event with the last stored eventId and updates the eventLiveData accordingly.
     */
    public void refreshEvent() {
        eventDataSource.getEvent(lastEventId).thenAccept(eventLiveData::setValue);
    }

    /**
     * Adds a user to the list of interested participants
     * If asInterested is true, it adds in the interested participants list
     * instead of the actual participants.
     *
     * @param userId       (String): id of the user to add to the interested list of event
     * @param asInterested (Boolean): To decide if rather add the user in the interested list or not
     * @return (LiveData < Boolean >): true if successfully added user to interested list
     */
    public LiveData<Boolean> joinEvent(String userId, boolean asInterested) {
        final MutableLiveData<Boolean> result = new MutableLiveData<>();
        if (lastEventId == null) {
            result.setValue(false);
            return result;
        }
        EventNotificationManager manager = new EventNotificationManager();
        manager.subscribeToEventTopic(lastEventId);

        eventDataSource.getEvent(lastEventId).thenAccept(event -> {
            final CompletableFuture<Boolean> future = asInterested ?
                    eventDataSource.joinEventAsInterested(event.getId(), userId) :
                    eventDataSource.joinEvent(event.getId(), userId);

            future.thenAccept(success -> {
                refreshEvent();
                result.setValue(success);
            });
        });
        return result;
    }

    /**
     * Removes completely the user from the event, also if he was just interested
     *
     * @param userId (String): id of the user to remove from the event
     * @return (LiveData < Boolean >): true if participation status has been left.
     */
    public LiveData<Boolean> leaveEvent(String userId) {
        final MutableLiveData<Boolean> result = new MutableLiveData<>();
        if (lastEventId == null) {
            result.setValue(false);
            return result;
        }
        EventNotificationManager manager = new EventNotificationManager();
        manager.unsubscribeFromEventTopic(lastEventId);

        eventDataSource.getEvent(lastEventId).thenAccept(event -> eventDataSource.leaveEvent(event.getId(), userId).thenAccept(b -> {
            refreshEvent();
            result.setValue(b);
        }));
        return result;
    }

    public void saveEventRestrictions(String eventId, Event.EventRestrictions restrictions) {
        eventDataSource.saveEventRestrictions(eventId, restrictions);
    }
}
