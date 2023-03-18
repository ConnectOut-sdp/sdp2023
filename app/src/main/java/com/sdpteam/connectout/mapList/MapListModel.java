package com.sdpteam.connectout.mapList;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sdpteam.connectout.event.Event;
import com.sdpteam.connectout.event.EventCreatorModel;

import java.util.ArrayList;
import java.util.List;

public class MapListModel implements MapListModelManager{
    private final DatabaseReference database;
    private final static int MAX_EVENTS_FETCHED = 50;

    private final MutableLiveData<List<Event>> cachedEvents;

    public MapListModel() {
        database = FirebaseDatabase.getInstance().getReference();
        cachedEvents = new MutableLiveData<>(new ArrayList<>());
    }

    @Override
    public MutableLiveData<List<Event>> getEventLiveList(String filteredAttribute, String expectedValue) {

        Query wantedEvents = database.child(EventCreatorModel.DATABASE_EVENT_PATH).limitToFirst(MAX_EVENTS_FETCHED);

        if (filteredAttribute != null && expectedValue != null) {
            wantedEvents = wantedEvents.orderByChild(filteredAttribute).startAt(expectedValue).endAt(expectedValue);
        }

        wantedEvents.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Event> eventList = new ArrayList<>();
                if(snapshot.exists() && snapshot.getChildrenCount() > 0){
                    snapshot.getChildren().forEach(child -> eventList.add(child.getValue(Event.class)));
                }
                cachedEvents.setValue(eventList);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.d("Error couldn't fetch events", error.getMessage());
            }
        });

        return cachedEvents;
    }
}
