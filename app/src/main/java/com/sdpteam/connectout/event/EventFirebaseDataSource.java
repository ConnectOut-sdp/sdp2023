package com.sdpteam.connectout.event;

import static com.sdpteam.connectout.profile.ProfileFirebaseDataSource.PROFILE;
import static com.sdpteam.connectout.profile.ProfileFirebaseDataSource.USERS;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.sdpteam.connectout.event.nearbyEvents.filter.BinaryFilter;
import com.sdpteam.connectout.event.nearbyEvents.filter.EventFilter;
import com.sdpteam.connectout.profile.Profile;
import com.sdpteam.connectout.profile.ProfileFirebaseDataSource;

public class EventFirebaseDataSource implements EventRepository {
    public final static String DATABASE_EVENT_PATH = "Events";
    private final static int MAX_EVENTS_FETCHED = 100;
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
        public CompletableFuture<List<Event>> getEventsByFilter(BinaryFilter filter) {
            CompletableFuture<List<Event>> future = new CompletableFuture<>();
            database.child(EventFirebaseDataSource.DATABASE_EVENT_PATH)
                    .limitToFirst(MAX_EVENTS_FETCHED).orderByKey()
                    .get()
                    .addOnCompleteListener(t -> {
                        List<Event> events = new ArrayList<>();
                        List<Task<?>> profileTasks = new ArrayList<>(); // Create a list to store all the profile tasks
                        for (DataSnapshot child : t.getResult().getChildren()) {
                            Event event = child.getValue(Event.class);
                            if (filter.testEvent(event)) {
                                List<Task<DataSnapshot>> tasks = new ArrayList<>();
                                for (String userId : event.getParticipants()) {
                                    tasks.add(database.child(USERS).child(userId).child(PROFILE).get());
                                }
                                Task<List<DataSnapshot>> allTasks = Tasks.whenAllSuccess(tasks);
                                profileTasks.add(allTasks); // Add the profile task to the list of profile tasks

                                allTasks.addOnSuccessListener(dataSnapshots -> {
                                    List<Profile> profiles = new ArrayList<>();
                                    for (DataSnapshot dataSnapshot : dataSnapshots) {
                                        Profile profile = dataSnapshot.getValue(Profile.class);
                                        profiles.add(profile);
                                    }
                                    if (filter.testParticipants(profiles)){
                                        events.add(event);
                                    }
                                });
                            }
                        }
                        // Wait for all profile tasks to complete, then complete the future with the events
                        Tasks.whenAllComplete(profileTasks).addOnCompleteListener(task -> future.complete(events));
                    });

            return future;
        }






}

