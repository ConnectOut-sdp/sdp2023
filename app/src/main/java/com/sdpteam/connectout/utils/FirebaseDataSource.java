package com.sdpteam.connectout.utils;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class FirebaseDataSource {
    public final static DatabaseReference firebaseRef;

    static {
        firebaseRef = FirebaseDatabase.getInstance().getReference();
    }
}
