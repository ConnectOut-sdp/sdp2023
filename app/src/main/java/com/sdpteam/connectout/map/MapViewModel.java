package com.sdpteam.connectout.map;

import java.util.List;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.sdpteam.connectout.event.Event;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class MapViewModel extends ViewModel implements OnMapReadyCallback {
    private MutableLiveData<GoogleMap> mapLiveData;
    private MutableLiveData<List<Event>> eventLiveList;

    private InterfaceMapModel mapModel;

    public void init(InterfaceMapModel model) {
        if (mapLiveData != null) {
            return;
        }
        mapModel = model;
        mapLiveData = new MutableLiveData<>();
        eventLiveList = mapModel.getEventLiveList();
    }

    public LiveData<List<Event>> getEventList() {
        return eventLiveList;
    }

    public void setEventList(MutableLiveData<List<Event>> eventLiveList) {
        if (eventLiveList == null) {
            return;
        }
        this.eventLiveList = eventLiveList;
    }

    /**
     * Calls on the model to modify the eventLiveList
     * In a later version, we could only perform the change if the list has not changed
     */
    public LiveData<List<Event>> refreshEventList() {
        setEventList(mapModel.getEventLiveList());
        return eventLiveList;
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mapLiveData.setValue(googleMap);
    }
}
