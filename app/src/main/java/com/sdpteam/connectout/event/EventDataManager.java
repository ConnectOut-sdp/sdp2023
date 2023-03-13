package com.sdpteam.connectout.event;

import androidx.lifecycle.LiveData;

public interface EventDataManager {
    /**
     * saves the given Event in the firebase database
     *
     * @param event (Event): the given event to save
     * @return (boolean): true if value is saved
     */
    boolean saveValue(Event event);

    /**
     * Fetches the given Event from the firebase database
     *
     * @param eid (String): unique identifier of the event
     * @return (LiveData < Event >): container that has the event uniquely identified event
     */
    LiveData<Event> getValue(String eid);

    /**
     * Fetches the given Event from the firebase database
     *
     * @param uid   (String): Id of the owner of the event
     * @param title (Title): title of the event
     * @return (LiveData < Event >): container that has the event uniquely identified event
     */
    LiveData<Event> getValue(String uid, String title);
}
