package com.sdpteam.connectout.profile;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReportModel {

    private final DatabaseReference firebaseRef;
    private final String reportPathString = "Report";

    public ReportModel() {
        firebaseRef = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * Saves a given report to firebase
     */
    public void saveReport(String report, String reportedUid, String reporterUid) {
        firebaseRef.child(reportPathString).child(reportedUid).child(reporterUid).setValue(report);
    }

    public void deleteProfile(String uid) {
        firebaseRef.child(reportPathString).child(uid).removeValue();
    }

    public DatabaseReference getDatabaseReference() {
        return firebaseRef;
    }
}
