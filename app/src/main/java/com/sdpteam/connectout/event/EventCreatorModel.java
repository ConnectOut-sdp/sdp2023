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
import com.sdpteam.connectout.mapList.MapListModel;
import com.sdpteam.connectout.mapList.MapListModelManager;

import java.util.concurrent.CompletableFuture;

public class EventCreatorModel extends MapListModel implements EventDataManager {
    public final static String DATABASE_EVENT_PATH = "Events";
    private final DatabaseReference database;

    public EventCreatorModel() {
        database = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * @inheritDoc
     * Saves the given Event in the firebase database
     *
     * @param event (Event): The given event to save
     * @return (boolean): True if value is saved
     *
     * /!\ the save return value will be useful for the offline mode /!\
     */
    @Override
    public boolean saveEvent(Event event) {
        if (event != null) {
            database.child(DATABASE_EVENT_PATH).child(event.getId()).setValue(event);
            return true;
        }
        return false;
    }

    /**
     * @inheritDoc
     * Fetches the wanted Event from the firebase database using its id.
     *
     * @param eventId (String): Unique identifier of the event
     * @return (LiveData<Event>): Container of the wanted event
     */
    @Override
    public LiveData<Event> getEvent(String eventId) {
        MutableLiveData<Event> value = new MutableLiveData<>();
        database.child(DATABASE_EVENT_PATH)
                .child(eventId)
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Event valueFromFirebase = dataSnapshot.getValue(Event.class);
                        value.setValue(valueFromFirebase);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(this.getClass().toString(), "Failed to read value.", databaseError.toException());
                    }

    });
        return value;
    }

    /**
     * @inheritDoc
     * Fetches the wanted Event from the firebase database using its owner id & title.
     *
     * @param userId   (String): Id of the owner of the event
     * @param title (Title): Title of the event
     * @return (LiveData<Event>): Container of the wanted event
     */
    @Override
    public LiveData<Event> getEvent(String userId, String title) {
        MutableLiveData<Event> value = new MutableLiveData<>();
        database.child(DATABASE_EVENT_PATH).orderByKey().addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Event matchingEvent = null;
                //Iterate on given children of the snapshot.
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    //Casts child into an event using default constructor.
                    Event e = snapshot.getValue(Event.class);
                    //Fetch the event with matching attributes.
                    if (userId.equals(e.getOrganizer()) && title.equals(e.getTitle())) {
                        matchingEvent = e;
                        break;
                    }
                }
                //Send the value back to the container
                value.setValue(matchingEvent);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(this.getClass().toString(), "Failed to find event.", databaseError.toException());
            }
        });
        return value;
    }

    /**
     *
     * @return (String): Event Id that is truly unique in the model.
     */
    public String getUniqueId(){
        return database.child(DATABASE_EVENT_PATH).push().getKey();
    }

}

