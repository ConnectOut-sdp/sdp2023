package com.sdpteam.connectout.map;

import java.util.List;

import androidx.lifecycle.MutableLiveData;

import com.sdpteam.connectout.event.Event;

public interface InterfaceMapModel {

    MutableLiveData<List<Event>> getEventLiveList();
}
