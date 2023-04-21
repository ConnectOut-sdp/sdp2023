package com.sdpteam.connectout.event;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.sdpteam.connectout.event.nearbyEvents.filter.EventFilter;
import com.sdpteam.connectout.event.nearbyEvents.filter.ProfilesFilter;
import com.sdpteam.connectout.profile.ProfileFirebaseDataSource;
import com.sdpteam.connectout.utils.FirebaseDataSource;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class EventFirebaseDataSource extends FirebaseDataSource implements EventRepository {
    public final static String DATABASE_EVENT_PATH = "Events";
    private final static int MAX_EVENTS_FETCHED = 100;

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
            firebaseRef.child(DATABASE_EVENT_PATH).child(event.getId()).setValue(event);
            return true;
        }
        return false;
    }

    /**
     * Enables data race free modification of an event through the eventModifier function.
     *
     * @param eventId       (String): Id of the referred event.
     * @param eventModifier (Function<Event, Event>): method which apply changes to the referred event, returns true if successful.
     * @return (CompletableFuture < Boolean >): upon completion will be true if applied change is successful
     */
    private CompletableFuture<Boolean> modifyEvent(String eventId, Function<Event, Boolean> eventModifier) {
        CompletableFuture<Boolean> future = new CompletableFuture<>();
        DatabaseReference eventRef = firebaseRef.child(DATABASE_EVENT_PATH).child(eventId);

        //Ask firebase to do the changes atomically.
        eventRef.runTransaction(new Transaction.Handler() {
            private boolean success;

            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {
                Event event = mutableData.getValue(Event.class);
                //Checks if event was casted correctly.
                if(event != null) {
                    success = eventModifier.apply(event);
                    mutableData.setValue(event);
                }
                return Transaction.success(mutableData);
            }

            @Override
            public void onComplete(DatabaseError databaseError, boolean committed, DataSnapshot dataSnapshot) {
                //Say if modification was successful
                future.complete(committed && success);
            }
        });
        return future;
    }

    /**
     *
     * @param eventId (String): Id of the event to which we add the participant
     * @param participantId (String): Id of the added participant
     * @return (CompletableFuture<Boolean>): completes to true if participant has joined the event.
     */
    public CompletableFuture<Boolean> joinEvent(String eventId, String participantId) {
        return modifyEvent(eventId, event -> event.addParticipant(participantId));
    }

    /**
     *
     * @param eventId (String): Id of the event to which we remove the participant
     * @param participantId (String): Id of the removed participant
     * @return (CompletableFuture<Boolean>): completes to true if participant has left the event.
     */
    public CompletableFuture<Boolean> leaveEvent(String eventId, String participantId) {
        return modifyEvent(eventId, event -> event.removeParticipant(participantId));
    }


    /**
     * @param eventId (String): Unique identifier of the event
     * @return (LiveData < Event >): Container of the wanted event
     * @inheritDoc Fetches the wanted Event from the firebase database using its id.
     */
    @Override
    public CompletableFuture<Event> getEvent(String eventId) {
        CompletableFuture<Event> future = new CompletableFuture<>();
        Task<DataSnapshot> task = firebaseRef.child(DATABASE_EVENT_PATH).child(eventId).get();
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
        Task<DataSnapshot> task = firebaseRef.child(DATABASE_EVENT_PATH).orderByKey().get();
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
        return firebaseRef.child(DATABASE_EVENT_PATH).push().getKey();
    }

    /**
     * @param eventFilter    (EventFilter): Custom filter to apply upon the event's attribute
     * @param profilesFilter (ProfilesFilter): Custom filter to apply upon the participants profile's attribute
     * @return (CompletableFuture < List < Event > >): a changeable list of different events.
     */
    @Override
    public CompletableFuture<List<Event>> getEventsByFilter(EventFilter eventFilter, ProfilesFilter profilesFilter) {
        CompletableFuture<List<Event>> future = new CompletableFuture<>();


        firebaseRef.child(EventFirebaseDataSource.DATABASE_EVENT_PATH)
                .limitToFirst(MAX_EVENTS_FETCHED).orderByKey()
                .get()
                .addOnCompleteListener(t -> {

                    ProfileFirebaseDataSource profileDatabase = new ProfileFirebaseDataSource();
                    List<Event> events = new ArrayList<>();
                    List<CompletableFuture<Void>> allProfilesFutures = new ArrayList<>();

                    for (DataSnapshot child : t.getResult().getChildren()) {
                        Event event = child.getValue(Event.class);
                        if (eventFilter.test(event)) {
                            CompletableFuture<Void> profilesFuture = profileDatabase.fetchProfiles(event.getParticipants())
                                    .thenAccept(profileList -> {
                                        if (profilesFilter.test(profileList)) {
                                            events.add(event);
                                        }
                                    });
                            allProfilesFutures.add(profilesFuture);

                        }
                    }
                    // Wait for all profile tasks to complete, then complete the future with the events
                    CompletableFuture.allOf(allProfilesFutures.toArray(new CompletableFuture[0])).whenComplete((unused, throwable) -> future.complete(events));
                });

        return future;
    }


}

