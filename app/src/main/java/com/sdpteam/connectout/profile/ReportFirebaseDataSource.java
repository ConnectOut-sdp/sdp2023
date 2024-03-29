package com.sdpteam.connectout.profile;

import java.util.concurrent.CompletableFuture;

import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ReportFirebaseDataSource {

    private final String reportPathString = "Report";
    private final DatabaseReference firebaseRef;

    public ReportFirebaseDataSource() {
        firebaseRef = FirebaseDatabase.getInstance().getReference();
    }

    /**
     * Saves a given report to firebase
     */
    public void saveReport(String report, String reportedUid, String reporterUid) {
        firebaseRef.child(reportPathString).child(reportedUid).child(reporterUid).setValue(report);
    }

    public void deleteReport(String uid) {
        firebaseRef.child(reportPathString).child(uid).removeValue();
    }

    public CompletableFuture<String> fetchReport(String reportedUid, String reporterUid) {
        // Get the value from Firebase
        CompletableFuture<String> future = new CompletableFuture<>();
        Task<DataSnapshot> dataSnapshotTask = firebaseRef.child(reportPathString).child(reportedUid).child(reporterUid).get();
        dataSnapshotTask.addOnCompleteListener(task -> {
            if (!task.isSuccessful()) {
                future.completeExceptionally(task.getException());
            }
            String valueFromFirebase = task.getResult().getValue(String.class);
            future.complete(valueFromFirebase);
        });
        return future;
    }
}
