package com.sdpteam.connectout;

import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.List;

public class MapModel implements InterfaceMapModel {
    private ArrayList<Event> dataSet = new ArrayList<>(); //To be defined at a later stage

    public MapModel() {
        //initialize dataSet
    }


    /*
     * return new List to the ViewModel
     * */
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
        testList.add(new Event("pin1", 46.521, 6.5678));
        testList.add(new Event("pin2", 46.5215, 6.56785));
        testList.add(new Event("pin3", 46.5218, 6.5679));

        dataSet = testList;
    }
}
