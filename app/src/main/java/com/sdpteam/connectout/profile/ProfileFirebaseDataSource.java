package com.sdpteam.connectout.profile;

import static com.sdpteam.connectout.profile.Profile.Gender.FEMALE;
import static com.sdpteam.connectout.profile.Profile.Gender.MALE;
import static com.sdpteam.connectout.profile.Profile.Gender.OTHER;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

public class ProfileFirebaseDataSource implements ProfileRepository {
    private final DatabaseReference firebaseRef;
    private final String USERS = "Users";
    private final String PROFILE = "Profile";

    public ProfileFirebaseDataSource() {
        firebaseRef = FirebaseDatabase.getInstance().getReference();
    }

    public void saveProfile(Profile profile) {
        firebaseRef.child(USERS).child(profile.getId()).child(PROFILE).setValue(profile);
    }

    public CompletableFuture<Profile> fetchProfile(String uid) {
        // Get the value from Firebase
        CompletableFuture<Profile> future = new CompletableFuture<>();
        Task<DataSnapshot> dataSnapshotTask = firebaseRef.child(USERS).child(uid).child(PROFILE).get();
        dataSnapshotTask.addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                future.completeExceptionally(task.getException());
            }
            Profile valueFromFirebase = task.getResult().getValue(Profile.class);
            future.complete(valueFromFirebase);
        });
        return future;
    }

    @Override
    public CompletableFuture<List<Profile>> getListOfUsers() {
        //TODO: get data from firebase

        List<Profile> userlist = new ArrayList<>();

        userlist.add(new Profile("100", "Alice", "Alice@gmail.com", "Hello, I'm Alice", FEMALE, 4.5, 10));
        userlist.add(new Profile("101", "Bob", "Bob@gmail.com", "Hello, I'm Bob", MALE, 3.5, 12));
        userlist.add(new Profile("102", "Charlie", "Charlie@gmail.com", "Hello, I'm Charlie", OTHER, 5, 3));

        return CompletableFuture.completedFuture(userlist);
    }

    public void deleteProfile(String uid) {
        firebaseRef.child(USERS).child(uid).child(PROFILE).removeValue();
    }
}

