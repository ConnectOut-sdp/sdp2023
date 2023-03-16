package com.sdpteam.connectout.map;

import androidx.lifecycle.MutableLiveData;

import com.sdpteam.connectout.event.Event;

import java.util.List;

public interface MapModelManager {

    MutableLiveData<List<Event>> getEventLiveList();
}
