package com.sdpteam.connectout.event;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.sdpteam.connectout.map.GPSCoordinates;

import java.util.Map;
import java.util.Objects;

public class EventCreatorModel implements EventDataManager{

    private final DatabaseReference database;

    public EventCreatorModel() {
        database = FirebaseDatabase.getInstance().getReference();
    }

    @Override
    public void saveValue(Event event) {
        database.child("Events").child(event.getEventId()).setValue(event);
    }

    @Override
    public LiveData<Event> getValue(String eid) {
        MutableLiveData<Event> value = new MutableLiveData<>();
        database.child("Events").child(eid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                /*Map<String, Object> o = (Map<String, Object>) dataSnapshot.getValue();
                Map<String, Double> coordinatesMap = (Map<String, Double>) o.get("gpsCoordinates");
                double latitude = coordinatesMap.get("latitude");
                double longitude = coordinatesMap.get("longitude");*/
                Event valueFromFirebase = dataSnapshot.getValue(Event.class);
                value.setValue(valueFromFirebase);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(this.getClass().toString(), "Failed to read event.", databaseError.toException());
            }
        });
        return value;
    }

    @Override
    public LiveData<Event> getValue(String uid, String title) {
        MutableLiveData<Event> value = new MutableLiveData<>();
        database.child("Events").orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                long count =  dataSnapshot.getChildrenCount();
                long i = 0;
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    i++;
                    Event e = snapshot.getValue(Event.class);
                    if (e!= null && uid.equals(e.getOwnerId()) && title.equals(e.getTitle())) {
                        value.setValue(e);
                        break;
                    }
                    if (i == count) {
                        value.setValue(null);
                        break;
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(this.getClass().toString(), "Failed to find event.", databaseError.toException());
            }
        });
        return value;
    }

    }

