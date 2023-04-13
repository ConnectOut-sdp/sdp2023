package com.sdpteam.connectout.event;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sdpteam.connectout.event.nearbyEvents.filter.EventFilter;

public class EventFirebaseDataSource implements EventRepository {
    public final static String DATABASE_EVENT_PATH = "Events";
    private final static int MAX_EVENTS_FETCHED = 50;
    private final DatabaseReference database;

    public EventFirebaseDataSource() {
        database = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * @param event (Event): The given event to save
     * @return (boolean): True if value is saved
     * <p>
     * /!\ the save return value will be useful for the offline mode /!\
     * @inheritDoc Saves the given Event in the firebase database
     */
    @Override
    public boolean saveEvent(Event event) {
        if (event != null) {
            database.child(DATABASE_EVENT_PATH).child(event.getId()).setValue(event);
            return true;
        }
        return false;
    }

    public void joinEvent(String eventId, String participantId) {
        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put("participants/" + UUID.randomUUID().hashCode(), participantId);
        database.child(DATABASE_EVENT_PATH).child(eventId).updateChildren(childUpdates);

    }
    public void leaveEvent(String eventId, String participantId) {
            database.child(DATABASE_EVENT_PATH).child(eventId).child("participants").child(participantId).removeValue();
    }


    /**
     * @param eventId (String): Unique identifier of the event
     * @return (LiveData < Event >): Container of the wanted event
     * @inheritDoc Fetches the wanted Event from the firebase database using its id.
     */
    @Override
    public CompletableFuture<Event> getEvent(String eventId) {
        CompletableFuture<Event> future = new CompletableFuture<>();
        Task<DataSnapshot> task = database.child(DATABASE_EVENT_PATH).child(eventId).get();
        task.addOnCompleteListener(t -> {
            Event valueFromFirebase = t.getResult().getValue(Event.class);
            future.complete(valueFromFirebase);
        });
        return future;
    }

    /**
     * @param userId (String): Id of the owner of the event
     * @param title  (Title): Title of the event
     * @return (LiveData < Event >): Container of the wanted event
     * @inheritDoc Fetches the wanted Event from the firebase database using its owner id & title.
     */
    @Override
    public CompletableFuture<Event> getEvent(String userId, String title) {
        CompletableFuture<Event> value = new CompletableFuture<>();
        Task<DataSnapshot> task = database.child(DATABASE_EVENT_PATH).orderByKey().get();
        task.addOnCompleteListener(t -> {
            Event matchingEvent = null;
            //Iterate on given children of the snapshot.
            for (DataSnapshot snapshot : t.getResult().getChildren()) {
                //Casts child into an event using default constructor.
                Event e = snapshot.getValue(Event.class);
                //Fetch the event with matching attributes.
                if (userId.equals(e.getOrganizer()) && title.equals(e.getTitle())) {
                    matchingEvent = e;
                    break;
                }
            }
            //Send the value back to the container
            value.complete(matchingEvent);
        });
        return value;
    }

    /**
     * @return (String): Event Id that is truly unique in the model.
     */
    public String getUniqueId() {
        return database.child(DATABASE_EVENT_PATH).push().getKey();
    }

    @Override
    public CompletableFuture<List<Event>> getEventsByFilter(EventFilter filter) {
        CompletableFuture<List<Event>> value = new CompletableFuture<>();
        Task<DataSnapshot> task = database.child(EventFirebaseDataSource.DATABASE_EVENT_PATH).limitToFirst(MAX_EVENTS_FETCHED).get();

        task.addOnCompleteListener(t -> {
            DataSnapshot snapshot = t.getResult();
            List<Event> eventList = new ArrayList<>();
            if (snapshot.exists() && snapshot.getChildrenCount() > 0) {
                snapshot.getChildren().forEach(child -> {
                    final Event event = child.getValue(Event.class);
                    if (filter.test(event)) {
                        eventList.add(event);
                    }
                });
            }
            value.complete(eventList);
        });
        return value;
    }
}

