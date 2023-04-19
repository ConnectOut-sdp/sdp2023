package com.sdpteam.connectout.profile;

import static com.sdpteam.connectout.profile.ProfileFirebaseDataSource.ProfileOrderingOption.*;
import android.view.View;
import android.widget.ListAdapter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import io.reactivex.rxjava3.annotations.NonNull;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class ProfileFirebaseDataSource implements ProfileRepository, RegisteredEventsRepository{
    private final DatabaseReference firebaseRef;
    private final static int MAX_PROFILES_FETCHED = 50;
    private final static String AUTOMATIC_COMPLETION_REGEX = "\uf8ff";
    private final String REGISTERED_EVENTS = "RegisteredEvents";
    private final int NUM_IMPORTED_EVENTS = 50;
    public final static String USERS = "Users";
    public final static String PROFILE = "Profile";

    public ProfileFirebaseDataSource() {
        firebaseRef = FirebaseDatabase.getInstance().getReference();
    }

    public void saveProfile(Profile profile) {
        firebaseRef.child(USERS)
                .child(profile
                        .getId())
                .child(PROFILE)
                .setValue(profile);
    }

    public CompletableFuture<Profile> fetchProfile(String uid) {
        CompletableFuture<Profile> future = new CompletableFuture<>();
        fetchProfiles(new ArrayList<>(Collections.singletonList(uid)))
                //List has at least a null element.
                .thenApply(profiles -> future.complete(profiles.get(0)));
        return future;
    }
    public CompletableFuture<List<Profile>> fetchProfiles(List<String> userIds) {
        CompletableFuture<List<Profile>> futures = new CompletableFuture<>();
        List<Task<DataSnapshot>> tasks = new ArrayList<>();

        for (String userId : userIds) {
            tasks.add(firebaseRef.child(USERS).child(userId).child(PROFILE).get());
        }

        Task<List<DataSnapshot>> allTasks = Tasks.whenAllSuccess(tasks);
        List<Profile> profiles = new ArrayList<>();
        allTasks.addOnCompleteListener(dataSnapshots -> {
            for (DataSnapshot dataSnapshot : dataSnapshots.getResult()) {
                Profile profile = dataSnapshot.getValue(Profile.class);
                profiles.add(profile);
            }
            futures.complete(profiles);
        });
        return futures;
    }


    /**
     * @param option (ProfileOrderingOption): option of filtering adopted, random, by name or by rating.
     * @param values (List<String>): list of parsed users inputs which corresponds to the filters.
     * @return (LiveData < List < Profile > >): List of all profiles found that matches the given filters.
     */
    @Override
    public CompletableFuture<List<Profile>> getListOfProfile(ProfileOrderingOption option, List<String> values) {
        CompletableFuture<List<Profile>> value = new CompletableFuture<>();
        Query query = filterProfiles(firebaseRef.child(USERS), option, values).limitToFirst(MAX_PROFILES_FETCHED);
        query.get().addOnCompleteListener( t->{
                    List<Profile> profilesList = new ArrayList<>();
                    DataSnapshot snapshot =  t.getResult();
                    snapshot.getChildren().forEach(profileSnapshot -> profilesList.add(profileSnapshot.child(PROFILE).getValue(Profile.class)));
                    if (option == RATING) { Collections.reverse(profilesList); }
                    value.complete(profilesList);
                }
        );
        return value;
    }

    /**
     *
     * @param root (Query): query to parametrise with filters.
     * @param option (ProfileOrderingOption): parametric ordering options of the query.
     * @param values (List<String>): list of parsed users inputs which corresponds to the filters.
     * @return (Query): query with all the needed filters.
     */
    public Query filterProfiles(Query root, ProfileOrderingOption option, List<String> values) {
        if(option == NONE){
            return root;
        }

        Query query = root.orderByChild(PROFILE + "/" + option.toString());

        if(values != null && values.size() > 0){
            if(option == NAME){
                query = filterByNameProfile(query,  values);
            }else {
                query = filterByRatingProfile(query, values);
            }
        }

        return query;
    }

    /**
     *
     * @param root (Query): given query to process
     * @param values (List<String>): possible rating to sort with
     * @return (Query): query that retrieves the desired number or number range.
     */
    private Query filterByRatingProfile(@NonNull Query root,@NonNull List<String> values){
        double ratingStart = Double.parseDouble(values.get(0));
        double ratingEnd;
        if(values.size() == 2) {
            ratingEnd = Double.parseDouble(values.get(1));
        } else {
            ratingEnd = ratingStart;
        }
        return root.startAt(ratingStart).endAt(ratingEnd);
    }

    /**
     *
     * @param root (Query): given query to process
     * @param values (List<String>): possible name to sort with
     * @return (Query): query that retrieves with the desired name.
     */
    private Query filterByNameProfile(Query root, List<String> values){
        String name = values.get(0).toLowerCase();

        //The regex is used to ensure that we retrieve all names starting with the given string.
        return  root.startAt(name).endAt(name + AUTOMATIC_COMPLETION_REGEX);
    }

    /**
     * Enum describing the different types of filtering possible.
     */
    public enum ProfileOrderingOption {
        NONE(""),
        RATING("rating"),
        NAME("nameLowercase");


        private final String name;

        ProfileOrderingOption(String name) {
            this.name = name;
        }

        @androidx.annotation.NonNull
        @Override
        public String toString() {
            return name;
        }
    }

    public void deleteProfile(String uid) {
        firebaseRef.child(USERS).child(uid).child(PROFILE).removeValue();
    }

    /**
     * stores a new Profile.CalendarEvent (eventId, eventTitle and eventDate)
     * in list of events that a profile is registered to
     *
     * this list of events is stored under USERS/profileId/REGISTERED_EVENTS
     * each CalendarEvent has an auto generated key
     *
     * We make sure that a Calendar Event isn't already in the User's memory before adding it
     * Not very efficient if the user is registered to A LOT of events. Which won't be the case so we re fine
     * */
    public void registerToEvent(Profile.CalendarEvent calEvent, String profileId){
        Query q = firebaseRef.child(USERS).child(profileId).child(REGISTERED_EVENTS).orderByChild("eventId")
                        .equalTo(calEvent.getEventId());
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    // The query returned a result, which means the event already exists in the user's registered events
                    // As such we don't do anything
                } else {
                    // The query did not return any results, which means the event does not exist in the user's registered events
                    // Push the event to the user's registered events in the Firebase database
                    firebaseRef.child(USERS).child(profileId).child(REGISTERED_EVENTS).push().setValue(calEvent);
                }
            }
            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                //do nothing
            }
        });

    }
    /**
     * sets up the FirebaseListAdapter for the registered events view
     *
     * Displays the List of Profile.CalendarEvent that is stored in Firebase under
     * USERS/profileId/REGISTERED_EVENTS
     *
     * orders the CalendarEvents by eventDate
     */
    public void setUpListAdapter(Function<FirebaseListOptions.Builder<Profile.CalendarEvent>, FirebaseListOptions.Builder<Profile.CalendarEvent>> setLayout,
                                 Function<FirebaseListOptions.Builder<Profile.CalendarEvent>, FirebaseListOptions.Builder<Profile.CalendarEvent>> setLifecycleOwner,
                                 BiConsumer<View, Profile.CalendarEvent> populateView,
                                 Consumer<ListAdapter> setAdapter, String profileId) {
        Query query = firebaseRef
                .child(USERS).child(profileId).child(REGISTERED_EVENTS).orderByChild("eventDate");

        FirebaseListOptions<Profile.CalendarEvent> options = setLifecycleOwner.apply(setLayout.apply(new FirebaseListOptions.Builder<>())
                .setQuery(query, Profile.CalendarEvent.class)).build();

        FirebaseListAdapter<Profile.CalendarEvent> adapter = new FirebaseListAdapter<Profile.CalendarEvent>(options) {
            @Override
            protected void populateView(@androidx.annotation.NonNull View v, @androidx.annotation.NonNull Profile.CalendarEvent calendarEvent, int position) {
                populateView.accept(v, calendarEvent);
            }
        };

        setAdapter.accept(adapter);
    }
}

