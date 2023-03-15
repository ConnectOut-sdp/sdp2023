package com.sdpteam.connectout.profile;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import android.util.Log;
import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

public class ProfileFirebaseDataSource implements ProfileDataManager {
    private final DatabaseReference mDatabase;
    private final String usersPathString = "Users";
    private final String profilePathString = "Profile";

    public ProfileFirebaseDataSource() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void saveValue(Profile profile) {
        mDatabase.child(usersPathString).child(profile.getId()).child(profilePathString).setValue(profile);
    }

    public LiveData<Profile> getValue(String uid) {
        // Get the value from Firebase
        MutableLiveData<Profile> value = new MutableLiveData<>();
        mDatabase.child(usersPathString).child(uid).child(profilePathString).addListenerForSingleValueEvent(new ValueEventListener() {
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

