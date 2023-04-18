package com.sdpteam.connectout.remoteStorage;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.net.Uri;
import android.util.Log;

public class FileStorageFirebase {

    private final StorageReference firebaseFilesStorage;

    public FileStorageFirebase() {
        this.firebaseFilesStorage = FirebaseStorage.getInstance().getReference();
    }

    /**
     * @param file file to upload to Storage Firebase
     * @return the url of the uploaded file
     */
    public CompletableFuture<String> uploadFile(Uri file, String fileExtension) {
        Log.e("FileStorageFirebase", "start uploading " + file.getPath());

        StorageReference fileReference = firebaseFilesStorage.child(System.currentTimeMillis() + "_" + UUID.randomUUID() + "." + fileExtension);
        CompletableFuture<String> result = new CompletableFuture<>();
        fileReference.putFile(file)
                .addOnCompleteListener(taskSnapshot -> {
                    if (taskSnapshot.isSuccessful()) {
                        fileReference.getDownloadUrl().addOnCompleteListener(task -> {
                            if (task.isSuccessful()) {
                                Uri downloadUri = task.getResult();
                                result.complete(downloadUri.toString());
                            } else {
                                result.complete("FileStorageFirebase" + "Error getting download URL" + task.getException().toString());
                                Log.e("FileStorageFirebase", "Error getting download URL", task.getException());
                            }
                        });
                    } else {
                        Log.e("FileStorageFirebase", "Error uploading file", taskSnapshot.getException());
                        result.complete("FileStorageFirebase" + "Error uploading file" + taskSnapshot.getException().toString());
                    }
                });
        return result;
    }
}
