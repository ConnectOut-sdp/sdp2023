package com.sdpteam.connectout.remoteStorage;

import java.util.UUID;
import java.util.concurrent.CompletableFuture;

import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import android.net.Uri;

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
        StorageReference fileReference = firebaseFilesStorage.child(UUID.randomUUID() + "." + fileExtension);
        CompletableFuture<Uri> result = new CompletableFuture<>();
        fileReference.putFile(file)
                .addOnCompleteListener(taskSnapshot -> {
                    // Get the URL of the uploaded file
                    fileReference.getDownloadUrl().addOnCompleteListener(task -> {
                                result.complete(task.isSuccessful() ? task.getResult() : null);
                            }
                    );
                });
        return result;
    }
}
