package com.sdpteam.connectout.remoteStorage;

import static com.sdpteam.connectout.utils.FutureUtils.fJoin;
import static org.junit.Assert.assertTrue;

import android.content.ContentResolver;
import android.content.res.Resources;
import android.net.Uri;

import androidx.test.espresso.intent.rule.IntentsTestRule;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.utils.TestActivity;

import org.junit.Rule;
import org.junit.Test;
import org.junit.runner.RunWith;

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
}