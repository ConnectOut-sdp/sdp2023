package com.sdpteam.connectout.mapList;

import androidx.lifecycle.MutableLiveData;

import com.sdpteam.connectout.event.Event;

import java.util.List;

public interface MapListModelManager {

    /**
     *
     * @param filteredAttribute (String): attribute upon which the events are filtered.
     * @param expectedValue (String): value of the attribute that is expected
     * @return (MutableLiveData<List<Event>>): a changeable list of different events.
     */
    MutableLiveData<List<Event>> getEventLiveList(String filteredAttribute, String expectedValue);
}
