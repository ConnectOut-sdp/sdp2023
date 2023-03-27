package com.sdpteam.connectout.map;

import java.util.List;

import com.sdpteam.connectout.event.Event;

import androidx.lifecycle.MutableLiveData;

public interface MapModelManager {

    MutableLiveData<List<Event>> getEventLiveList();
}
