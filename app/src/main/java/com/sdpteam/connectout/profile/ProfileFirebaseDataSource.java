package com.sdpteam.connectout.profile;

import static com.sdpteam.connectout.profile.Profile.Gender.FEMALE;
import static com.sdpteam.connectout.profile.Profile.Gender.MALE;
import static com.sdpteam.connectout.profile.Profile.Gender.OTHER;
import static com.sdpteam.connectout.profileList.OrderingOption.*;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.sdpteam.connectout.profileList.OrderingOption;

public class ProfileFirebaseDataSource implements ProfileRepository {
    private final DatabaseReference firebaseRef;
    private final static int MAX_PROFILES_FETCHED = 50;
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
    public CompletableFuture<List<Profile>> getListOfProfile(OrderingOption option, List<String> values) {
        CompletableFuture<List<Profile>> value = new CompletableFuture<>();
        Task<DataSnapshot> task = filterProfiles(firebaseRef.child(USERS), option, values).limitToFirst(MAX_PROFILES_FETCHED).get();
        task.addOnCompleteListener( t->{
                    List<Profile> profilesList = new ArrayList<>();
                    DataSnapshot snapshot =  t.getResult();
                    snapshot.getChildren().forEach(profileSnapshot -> profilesList.add(profileSnapshot.child(PROFILE).getValue(Profile.class)));
                    value.complete(profilesList);
                }
        );
        return value;
    }

    public Query filterProfiles(Query root, OrderingOption option, List<String> values) {
        if(option == NONE){
            return root;
        }

        Query query = root.orderByChild(PROFILE + "/" + option.toString());

        if (option == NAME && values != null && values.size() == 1) {
            String name = values.get(0);
            query = query.startAt(name).endAt(name + "\uf8ff");
        } else if (option == RATING && values != null) {
            if(values.size() == 2) {
                double ratingStart = Double.parseDouble(values.get(0));
                double ratingEnd = Double.parseDouble(values.get(1));
                query = query.startAt(ratingStart).endAt(ratingEnd);
            } else if (values.size() == 1) {
                double rating = Double.parseDouble(values.get(0));
                query = query.equalTo(rating);
            }
        }

        return query;
    }
}

