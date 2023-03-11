package com.sdpteam.connectout.event;

import androidx.lifecycle.LiveData;

import com.sdpteam.connectout.profile.Profile;

public interface EventDataManager {
    /**
     * saves the given Event in the firebase database
     */
    void saveValue(Event event);

    /**
     * Fetches the given Event from the firebase database
     */
    LiveData<Event> getValue(String eid);

    /**
     * Fetches the given Event from the firebase database
     */
    LiveData<Event> getValue(String uid, String title);
}
