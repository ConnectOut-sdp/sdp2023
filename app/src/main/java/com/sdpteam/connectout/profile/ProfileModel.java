package com.sdpteam.connectout.profile;

import static android.content.ContentValues.TAG;

import static com.sdpteam.connectout.userList.OrderingOption.NAME;
import static com.sdpteam.connectout.userList.OrderingOption.NONE;
import static com.sdpteam.connectout.userList.OrderingOption.RATING;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.sdpteam.connectout.Path;
import com.sdpteam.connectout.userList.OrderingOption;
import com.sdpteam.connectout.userList.ProfileListDataManager;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class ProfileModel implements ProfileDataManager, ProfileListDataManager {
    private final DatabaseReference database;
    //Caches all the different profiles that have been perceived previously
    private static final Map<String, Profile> CACHED_PROFILES = new HashMap<>();

    public ProfileModel() {
        database = FirebaseDatabase.getInstance().getReference();
    }

    public void saveProfile(Profile profile) {
        CACHED_PROFILES.put(profile.getId(), profile);
        database.child(Path.Users.toString()).child(profile.getId()).child(Path.Profile.toString()).setValue(profile);
    }

    public LiveData<Profile> fetchProfile(String uid) {
        // Get the value from Firebase
        MutableLiveData<Profile> value = new MutableLiveData<>(CACHED_PROFILES.get(uid));
        database.child(Path.Users.toString()).child(uid).child(Path.Profile.toString()).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Profile valueFromFirebase = dataSnapshot.getValue(Profile.class);
                value.setValue(valueFromFirebase);
                CACHED_PROFILES.put(uid, valueFromFirebase);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Log.e(this.getClass().toString(), "Failed to read value.", databaseError.toException());
            }
        });
        return value;
    }

    @Override
    public MutableLiveData<List<Profile>> getListOfProfile(OrderingOption option, List<String> values) {
        MutableLiveData<List<Profile>> value = new MutableLiveData<>(CACHED_PROFILES.values().stream().limit(50).collect(Collectors.toList()));
        Query wantedProfiles = filterProfiles(database.child(Path.Users.toString()), option, values).limitToFirst(50);
        wantedProfiles.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                List<Profile> profilesList = new ArrayList<>();
                for (DataSnapshot profileSnapshot : snapshot.getChildren()) {
                    Profile profile = profileSnapshot.child(Path.Profile.toString())
                            .getValue(Profile.class);
                    profilesList.add(profile);
                    CACHED_PROFILES.put(profile.getId(), profile);
                }
                value.setValue(profilesList);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e(TAG, "Failed to retrieve profiles.", error.toException());
            }
        });

        return value;
    }

    public Query filterProfiles(Query root, OrderingOption option, List<String> values) {
        Query query = root;
        if(option == NONE){
            return root;
        }

        query = query.orderByChild(Path.Profile.toString() + Path.Slash + option.toString());

        if(values == null){
            return query;
        }else if(values.size() == 1 && option == NAME){
            return query.startAt(values.get(0)).endAt(values.get(0)+ "\uf8ff");
        }
        if (values.size() == 2 && option == RATING) {
            return query.startAt(Double.parseDouble(values.get(0))).endAt(Double.parseDouble(values.get(1)));
        }

        return query;
    }



}

