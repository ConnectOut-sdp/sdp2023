package com.sdpteam.connectout.profile;

import android.view.View;
import android.widget.ListAdapter;

import com.firebase.ui.database.FirebaseListAdapter;
import com.firebase.ui.database.FirebaseListOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sdpteam.connectout.profileList.filter.ProfileFilter;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class ProfileFirebaseDataSource implements ProfileDataSource, RegisteredEventsDataSource {
    public final static String USERS = "Users";
    public final static String PROFILE = "Profile";
    private final static int MAX_PROFILES_FETCHED = 50;
    private final String REGISTERED_EVENTS = "RegisteredEvents";
    private final DatabaseReference firebaseRef;

    public ProfileFirebaseDataSource() {
        firebaseRef = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * Saves the given profile in firebase
     *
     * @param profile (Profile): profile to save into firebase
     * @return (CompletableFuture < Boolean >): completes to true if profile is saved
     */
    @Override
    public CompletableFuture<Boolean> saveProfile(Profile profile) {
        CompletableFuture<Boolean> finished = new CompletableFuture<>();
        firebaseRef.child(USERS)
                .child(profile.getId())
                .child(PROFILE)
                .setValue(profile)
                .addOnCompleteListener(command -> finished.complete(command.isSuccessful()));
        return finished;
    }

    /**
     * Retrieves a Profile from firebase using its Id.
     *
     * @param userId (String): id of the user to find
     * @return (CompletableFuture < Profile >): completes to the matching profile Id
     */
    @Override
    public CompletableFuture<Profile> fetchProfile(String userId) {
        CompletableFuture<Profile> future = new CompletableFuture<>();
        fetchProfiles(new ArrayList<>(Collections.singletonList(userId)))
                //List has at least a null element.
                .thenApply(profiles -> future.complete(profiles.get(0)));
        return future;
    }

    /**
     * Retrieves a list of profiles from firebase using their Ids.
     *
     * @param userIds (List<String>): id of the user to find
     * @return (CompletableFuture < List < Profile > >): completes to the matching list of profiles Ids
     */
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
                if(profile != null) {
                    profiles.add(profile);
                }
            }
            futures.complete(profiles);
        });
        return futures;
    }

    @Override
    public CompletableFuture<List<Profile>> getProfilesByFilter(ProfileFilter filter) {
        final CompletableFuture<List<Profile>> future = new CompletableFuture<>();

        // Fetch all the users (profile + its registered events)
        firebaseRef.child(USERS).limitToFirst(MAX_PROFILES_FETCHED).get().addOnCompleteListener(t -> {
            final List<Profile> result = new ArrayList<>();

            // Iterate over the results
            for (DataSnapshot snapshot : t.getResult().getChildren()) {

                // Get the profile class
                final Profile profile = snapshot.child(PROFILE).getValue(Profile.class);
                if (profile == null || profile.getNameLowercase() == null) {
                    continue;
                }

                // Get the registered events list
                final List<RegisteredEvent> registeredEvents = new ArrayList<>();
                for (DataSnapshot child : snapshot.child(REGISTERED_EVENTS).getChildren()) {
                    final RegisteredEvent event = child.getValue(RegisteredEvent.class);
                    registeredEvents.add(event);
                }

                // Check if the entry satisfies the filter
                final ProfileEntry entry = new ProfileEntry(profile, registeredEvents);
                if (filter.test(entry)) {
                    result.add(entry.getProfile());
                }
            }
            future.complete(result);
        });
        return future;
    }

    public void deleteProfile(String uid) {
        firebaseRef.child(USERS).child(uid).removeValue();
    }

    /**
     * stores a new Profile.RegisteredEvent (eventId, eventTitle and eventDate)
     * in list of events that a profile is registered to
     * this list of events is stored under USERS/profileId/REGISTERED_EVENTS
     * each RegisteredEvent has an auto generated key
     * We make sure that a Calendar Event isn't already in the User's memory before adding it
     * Not very efficient if the user is registered to A LOT of events. Which won't be the case so we re fine
     */
    public void registerToEvent(RegisteredEvent calEvent, String profileId) {
        Query q = firebaseRef.child(USERS)
                .child(profileId)
                .child(REGISTERED_EVENTS)
                .child(calEvent.getEventId());
        q.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (!dataSnapshot.exists()) {
                    firebaseRef.child(USERS)
                            .child(profileId)
                            .child(REGISTERED_EVENTS)
                            .child(calEvent.getEventId())
                            .setValue(calEvent);
                }
            }

            @Override
            public void onCancelled(@androidx.annotation.NonNull DatabaseError error) {
                //do nothing
            }
        });
    }

    public void unregisterToEvent(String eventId, String profileId) {
        firebaseRef.child(USERS)
                .child(profileId)
                .child(REGISTERED_EVENTS)
                .child(eventId).removeValue();
    }
}

