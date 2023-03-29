package com.sdpteam.connectout.profile;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

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
        CompletableFuture<List<Profile>> future = new CompletableFuture<>();
        Task<DataSnapshot> task = firebaseRef.child(USERS).get();

        task.addOnCompleteListener(t -> {
            List<Profile> profiles = new ArrayList<>();
            DataSnapshot snapshot = t.getResult();

            for (DataSnapshot childSnapshot : snapshot.getChildren()) {// iterate over user ids
                //each user id has only one  children, called profile
                Profile profile = childSnapshot.getChildren().iterator().next().getValue(Profile.class);
                profiles.add(profile);
            }
            future.complete(profiles);
        });
        return future;
    }
}

