package com.sdpteam.connectout.event;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public interface EventRepository {

    /**
     * Saves the given Event in the firebase database
     *
     * @param event (Event): The given event to save
     * @return (boolean): True if value is saved
     *
     * /!\ the save return value will be useful for the offline mode /!\
     */
    boolean saveEvent(Event event);

    /**
     * Fetches the wanted Event from the firebase database using its id.
     *
     * @param eventId (String): Unique identifier of the event
     * @return (LiveData<Event>): Container of the wanted event
     */
    CompletableFuture<Event> getEvent(String eventId);

    /**
     * Fetches the wanted Event from the firebase database using its owner id & title.
     *
     * @param userId (String): Id of the owner of the event
     * @param title  (Title): Title of the event
     * @return (LiveData < Event >): Container of the wanted event
     */
    CompletableFuture<Event> getEvent(String userId, String title);

    /**
     *
     * @return (String): Event Id that is truly unique in the model.
     */
    String getUniqueId();

    /**
     * @param filteredAttribute (String): attribute upon which the events are filtered.
     * @param expectedValue     (String): value of the attribute that is expected
     * @return (MutableLiveData < List < Event > >): a changeable list of different events.
     */
    CompletableFuture<List<Event>> getEventLiveList(String filteredAttribute, String expectedValue);
}
