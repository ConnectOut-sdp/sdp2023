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

public class EventCreatorModel implements EventDataManager {
    public final static String DATABASE_EVENT_PATH  = "Events";
    private final DatabaseReference database;

    public EventCreatorModel() {
        database = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * @inheritDoc
     */
    @Override
    public boolean saveValue(Event event) {
        if(event != null) {
            database.child(DATABASE_EVENT_PATH).child(event.getEventId()).setValue(event);
            return true;
        }
        return false;
    }

    /**
     * @inheritDoc
     */
    @Override
    public LiveData<Event> getValue(String eid) {
        MutableLiveData<Event> value = new MutableLiveData<>();
        database.child(DATABASE_EVENT_PATH).child(eid).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
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

    /**
     * @inheritDoc
     */
    @Override
    public LiveData<Event> getValue(String uid, String title) {
        MutableLiveData<Event> value = new MutableLiveData<>();
        database.child(DATABASE_EVENT_PATH).orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Find current children count in snapshot.
                long count = dataSnapshot.getChildrenCount();
                long i = 0;
                //Iterate on future possible children & given ones.
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    i++;
                    //Casts child into an event using default constructor.
                    Event e = snapshot.getValue(Event.class);
                    //Fetch the one with matching attributes
                    if (uid.equals(e.getOwnerId()) && title.equals(e.getTitle())) {
                        value.setValue(e);
                        break;
                    }
                    //Upon reach of the last given child do not wait longer.
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

