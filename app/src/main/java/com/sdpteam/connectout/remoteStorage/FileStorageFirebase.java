package com.sdpteam.connectout.remoteStorage;

import android.net.Uri;
import android.util.Log;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

public class FileStorageFirebase {

    private final StorageReference firebaseFilesStorage;

    public FileStorageFirebase() {
        this.firebaseFilesStorage = FirebaseStorage.getInstance().getReference();
    }

    /**
     * @param file file to upload to Storage Firebase
     * @return the url of the uploaded file
     */
    public CompletableFuture<Uri> uploadFile(Uri file, String fileExtension) {
        StorageReference fileReference = firebaseFilesStorage.child(System.currentTimeMillis() + "_" + UUID.randomUUID() + "." + fileExtension);
        CompletableFuture<Uri> result = new CompletableFuture<>();
        fileReference.putFile(file)
                .addOnCompleteListener(taskSnapshot -> {
                    if (taskSnapshot.isSuccessful()) {
                        fileReference.getDownloadUrl().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                result.complete(downloadUri);
                            } else {
                                Log.e("FileStorageFirebase", "Error getting download URL", task.getException());
                                result.complete(null);
                            }
                        });
                    } else {
                        Log.e("FileStorageFirebase", "Error uploading file", taskSnapshot.getException());
                        result.complete(null);
                    }
                });
        return result;
    }
}
