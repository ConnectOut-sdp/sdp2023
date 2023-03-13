package com.sdpteam.connectout;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

public class IdValueListener<E> implements ValueEventListener {
    //Required for the getter
    private final Class<E> type;
    private final MutableLiveData<E> value;


    public IdValueListener(Class<E> type, MutableLiveData<E> value) {
        this.type = type;
        this.value = value;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        E valueFromFirebase = dataSnapshot.getValue(type);
        value.setValue(valueFromFirebase);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        Log.e(this.getClass().toString(), "Failed to read value.", databaseError.toException());
    }
}
