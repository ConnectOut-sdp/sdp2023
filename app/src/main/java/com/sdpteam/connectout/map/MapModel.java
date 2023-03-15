package com.sdpteam.connectout.map;

import androidx.lifecycle.MutableLiveData;

import com.sdpteam.connectout.event.Event;

import java.util.List;

public interface MapModel {

    MutableLiveData<List<Event>> getEventLiveList();
}
