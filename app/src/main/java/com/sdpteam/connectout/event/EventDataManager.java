package com.sdpteam.connectout.event;

import androidx.lifecycle.LiveData;

public interface EventDataManager {
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
    LiveData<Event> getEvent(String eventId);

    /**
     * Fetches the wanted Event from the firebase database using its owner id & title.
     *
     * @param userId   (String): Id of the owner of the event
     * @param title (Title): Title of the event
     * @return (LiveData<Event>): Container of the wanted event
     */
    LiveData<Event> getEvent(String userId, String title);

    /**
     *
     * @return (String): Event Id that is truly unique in the model.
     */
    String getUniqueId();
}
