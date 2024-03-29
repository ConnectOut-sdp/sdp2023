package com.sdpteam.connectout.remoteStorage;

import static com.sdpteam.connectout.utils.FutureUtils.fJoin;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.utils.TestActivity;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.net.Uri;
import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

@RunWith(AndroidJUnit4.class)
public class FileStorageFirebaseTest {

    @Rule
    public IntentsTestRule<TestActivity> intentsTestRule = new IntentsTestRule<>(TestActivity.class);

    @Test
    public void testUploadImageToFirebase() {
        Resources resources = intentsTestRule.getActivity().getResources();

        // Create the resource Uri for the event_image drawable
        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(R.drawable.event_image) + '/' +
                resources.getResourceTypeName(R.drawable.event_image) + '/' +
                resources.getResourceEntryName(R.drawable.event_image));
        if (uri == null) {
            throw new IllegalStateException("Why is it null?");
        }
        Uri createdUrl = fJoin(new FileStorageFirebase().uploadFile(uri, "jpg"));
        assertTrue(createdUrl.toString().contains("https://"));
    }

    @Test
    public void testUploadImagesToFirebase() {
        Resources resources = intentsTestRule.getActivity().getResources();

        // Create the resource Uri for the event_image drawable
        Uri uri = Uri.parse(ContentResolver.SCHEME_ANDROID_RESOURCE + "://" +
                resources.getResourcePackageName(R.drawable.event_image) + '/' +
                resources.getResourceTypeName(R.drawable.event_image) + '/' +
                resources.getResourceEntryName(R.drawable.event_image));
        if (uri == null) {
            throw new IllegalStateException("Why is it null?");
        }
        List<Uri> createdUrl = fJoin(new FileStorageFirebase().uploadImages(Arrays.asList(uri, uri)));
        assertTrue(createdUrl.get(0).toString().contains("https://"));
        assertTrue(createdUrl.get(1).toString().contains("https://"));
    }
}