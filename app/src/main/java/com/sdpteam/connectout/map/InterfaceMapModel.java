package com.sdpteam.connectout.map;

import androidx.lifecycle.MutableLiveData;

import com.sdpteam.connectout.event.Event;

import java.util.List;

public interface InterfaceMapModel {

    MutableLiveData<List<Event>> getEventLiveList();
}
