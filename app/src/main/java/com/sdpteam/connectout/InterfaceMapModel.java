package com.sdpteam.connectout;

import androidx.lifecycle.MutableLiveData;

import java.util.List;

public interface InterfaceMapModel {

    MutableLiveData<List<Event>> getEventLiveList();
}
