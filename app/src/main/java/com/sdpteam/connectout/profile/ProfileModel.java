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
import com.sdpteam.connectout.IdValueListener;
import com.sdpteam.connectout.authentication.AuthenticatedUser;
import com.sdpteam.connectout.authentication.GoogleAuth;

public class ProfileModel implements ProfileDataManager {
    private final DatabaseReference mDatabase;

    public ProfileModel() {
        mDatabase = FirebaseDatabase.getInstance().getReference();
    }

    public void saveValue(Profile profile, String uid) {
        mDatabase.child("unique id").child(uid).child("Profile").setValue(profile);
    }

    public LiveData<Profile> getValue(String uid) {
        // Get the value from Firebase
        MutableLiveData<Profile> value = new MutableLiveData<>();
        mDatabase.child("unique id")
                .child(uid)
                .child("Profile")
                .addListenerForSingleValueEvent(new IdValueListener<>(Profile.class,value));
        return value;
    }
}

