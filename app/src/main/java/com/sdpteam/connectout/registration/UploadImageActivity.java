package com.sdpteam.connectout.registration;

import java.io.IOException;

import com.google.firebase.FirebaseApp;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.sdpteam.connectout.R;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

// TODO tmp it is just a proof of concept
public class UploadImageActivity extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    private ImageView mImageView;
    private Button mChooseButton;
    private Button mUploadButton;
    private Uri mImageUri;

    private DatabaseReference mDatabaseRef;
    private StorageReference mStorageRef;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        mImageView = findViewById(R.id.image_view);
        mChooseButton = findViewById(R.id.choose_button);
        mUploadButton = findViewById(R.id.upload_button);
        mStorageRef = FirebaseStorage.getInstance().getReference("uploads");
        mDatabaseRef = FirebaseDatabase.getInstance().getReference("uploads");

        mChooseButton.setOnClickListener(view -> openFileChooser());

        mUploadButton.setOnClickListener(view -> uploadFile());
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            mImageUri = data.getData();

            try {
                mImageView.setImageBitmap(MediaStore.Images.Media.getBitmap(getContentResolver(), mImageUri));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void uploadFile() {
        if (mImageUri != null) {
            StorageReference fileReference = mStorageRef.child(System.currentTimeMillis() + ".jpg");

            fileReference.putFile(mImageUri)
                    .addOnSuccessListener(taskSnapshot -> {
                        // Get the URL of the uploaded file
                        fileReference.getDownloadUrl().addOnSuccessListener(uri -> {
                            Upload upload = new Upload(uri.toString());
                            String uploadId = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadId).setValue(upload);
                            Toast.makeText(UploadImageActivity.this, "Upload successful", Toast.LENGTH_SHORT).show();
                        });
                    })
                    .addOnFailureListener(e -> {
                        Log.d("TAG", "onFailure: " + e.getMessage());
                        Toast.makeText(UploadImageActivity.this, "Upload failed", Toast.LENGTH_SHORT).show();
                    });
        } else {
            Toast.makeText(this, "No file selected", Toast.LENGTH_SHORT).show();
        }
    }

    public class Upload {
        private String mImageUrl;

        public Upload() {
            // empty constructor needed for Firebase Realtime Database
        }

        public Upload(String imageUrl) {
            mImageUrl = imageUrl;
        }

        public String getImageUrl() {
            return mImageUrl;
        }

        public void setImageUrl(String imageUrl) {
            mImageUrl = imageUrl;
        }
    }

}
