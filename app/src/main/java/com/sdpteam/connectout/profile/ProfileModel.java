package com.sdpteam.connectout.profile;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileModel implements ProfileDataManager {
    private final DatabaseReference database;
    private final static String[] DATABASE_PROFILE_PATH = {"userId", "Profile"};

    public ProfileModel() {
        database = FirebaseDatabase.getInstance().getReference();
    }

    public void saveValue(Profile profile, String uid) {
        database.child(DATABASE_PROFILE_PATH[0]).child(uid).child(DATABASE_PROFILE_PATH[1]).setValue(profile);
    }

    public LiveData<Profile> getProfile(String uid) {
        // Get the value from Firebase
        MutableLiveData<Profile> value = new MutableLiveData<>();
        database.child(DATABASE_PROFILE_PATH[0])
                .child(uid)
                .child(DATABASE_PROFILE_PATH[1])
                .addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Profile valueFromFirebase = dataSnapshot.getValue(Profile.class);
                        value.setValue(valueFromFirebase);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {
                        Log.e(this.getClass().toString(), "Failed to read value.", databaseError.toException());
                    }
                });
        return value;
    }

}

