package com.sdpteam.connectout.map;

import java.util.ArrayList;
import java.util.List;

import androidx.lifecycle.MutableLiveData;

import com.sdpteam.connectout.event.Event;

public class MapModel implements InterfaceMapModel {
    private ArrayList<Event> dataSet = new ArrayList<>(); //To be defined at a later stage

    /*
     * return new List to the ViewModel
     * */
    @Override
    public MutableLiveData<List<Event>> getEventLiveList() {
        updateData();
        MutableLiveData<List<Event>> data = new MutableLiveData<>();
        data.setValue(dataSet);
        return data;
    }

    /*
     * Get data from a webservice
     * */
    private void updateData() {
        ArrayList<Event> testList = new ArrayList<>();
        testList.add(new Event("pin1", 46.521, 6.5678, "A1"));
        testList.add(new Event("pin2", 46.5215, 6.56785, "A2"));
        testList.add(new Event("pin3", 46.5218, 6.5679, "A3"));

        dataSet = testList;
    }
}
