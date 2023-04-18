package com.sdpteam.connectout.aaaremoteStorage;

import static org.hamcrest.Matchers.is;
import static org.junit.Assert.assertThat;
import static org.junit.Assert.assertTrue;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sdpteam.connectout.remoteStorage.FileStorageFirebase;
import com.sdpteam.connectout.utils.TestActivity;

import android.net.Uri;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class FileStorageFirebaseTest {

    @Rule
    public IntentsTestRule<TestActivity> intentsTestRule = new IntentsTestRule<>(TestActivity.class);

    @Test
    public void testUploadImageToFirebase() {
//        Resources resources = intentsTestRule.getActivity().getResources();
//
//        // Create the resource Uri for the event_image drawable
//        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
//                resources.getResourcePackageName(R.drawable.event_image) + '/' +
//                resources.getResourceTypeName(R.drawable.event_image) + '/' +
//                resources.getResourceEntryName(R.drawable.event_image));
//        if (uri == null) {
//            throw new IllegalStateException("Why is it null?");
//        }
        try {
            File tempFile = File.createTempFile("test", ".jpg");
            tempFile.deleteOnExit();

            // Write some test data to the file
            FileWriter writer = new FileWriter(tempFile);
            writer.write("This is a test file.");
            writer.close();

            // Upload the file to Firebase Storage
            Uri uri = Uri.fromFile(tempFile);

            String createdUrl = new FileStorageFirebase().uploadFile(uri, "jpg").join();
            assertThat(createdUrl.toString(), is("cououc"));
            assertTrue(createdUrl.toString().contains("https://"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}