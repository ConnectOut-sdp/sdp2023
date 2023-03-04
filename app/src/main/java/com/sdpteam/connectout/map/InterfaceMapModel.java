package com.sdpteam.connectout.map;

import java.util.List;

import androidx.lifecycle.MutableLiveData;

public interface InterfaceMapModel {

    MutableLiveData<List<Event>> getEventLiveList();
}
