package com.sdpteam.connectout.post.view;

import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.post.model.PostFirebaseDataSource;
import com.sdpteam.connectout.remoteStorage.ImageSelectionFragment;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.Button;
import android.widget.Switch;
import android.widget.TextView;
import androidx.annotation.IdRes;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.FragmentTransaction;

public class PostCreatorActivity extends AppCompatActivity {

    public static boolean TEST = false;
    PostCreatorViewModel viewModel;
    private Uri selectedImage1;
    private Uri selectedImage2;
    private Uri selectedImage3;

    /**
     * Helper method to launch a this activity from the source context
     * (made it to avoid code duplication)
     *
     * @param fromContext from where we are starting the intent
     */
    public static void openPostCreator(Context fromContext, String profileId, String eventId, String eventName) {
        Intent intent = new Intent(fromContext, PostCreatorActivity.class);
        intent.putExtra("profileId", profileId);
        intent.putExtra("eventId", eventId);
        intent.putExtra("eventName", eventName);
        fromContext.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_creator);

        final String profileId = Objects.requireNonNull(getIntent().getStringExtra("profileId"));
        final String eventId = Objects.requireNonNull(getIntent().getStringExtra("eventId"));
        final String eventName = Objects.requireNonNull(getIntent().getStringExtra("eventName"));

        if (viewModel == null) {
            viewModel = new PostCreatorViewModel(new PostFirebaseDataSource(), profileId);
        }

        initToolbar();
        initTitle(eventName);
        initImageSelectors();

        final TextView statusView = findViewById(R.id.post_creator_status);
        viewModel.statusMsgLiveData().observeForever(stringResult -> {
            statusView.setText(stringResult.msg());
            // main thread:
            if (stringResult.isSuccess() && !TEST) {
                new android.os.Handler().postDelayed(this::finish, 1000);
            }
        });

        final Switch visibilitySwitch = findViewById(R.id.post_creator_visibilitySwitch);
        final Button submitBtn = findViewById(R.id.post_creator_save_button);
        final TextView title = findViewById(R.id.post_creator_title);
        final TextView desc = findViewById(R.id.post_creator_description);
        submitBtn.setOnClickListener(v -> {
            String postTitle = title.getText().toString();
            String postDesc = desc.getText().toString();
            boolean isPublic = visibilitySwitch.isChecked();
            List<Uri> selectedImages = Arrays.asList(selectedImage1, selectedImage2, selectedImage3);
            statusView.setText("Starting...");
            viewModel.createPost(eventId, postTitle, postDesc, isPublic, selectedImages);
        });
    }

    private void initImageSelectors() {
        imageSelectorInit(imageUri -> selectedImage1 = imageUri, R.id.post_creator_image_1_container);
        imageSelectorInit(imageUri -> selectedImage2 = imageUri, R.id.post_creator_image_2_container);
        imageSelectorInit(imageUri -> selectedImage3 = imageUri, R.id.post_creator_image_3_container);
    }

    private void imageSelectorInit(ImageSelectionFragment.OnImageSelectedListener onImageSelected, @IdRes int containerViewId) {
        ImageSelectionFragment imageSelectionFragment = new ImageSelectionFragment();
        imageSelectionFragment.setOnImageSelectedListener(onImageSelected);
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(containerViewId, imageSelectionFragment);
        transaction.commit();
    }

    private void initTitle(String eventName) {
        TextView t = findViewById(R.id.post_creator_title_description);
        t.setText("Creating a post for event \"" + eventName + "\"");
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.event_creator_toolbar);
        if (toolbar == null) {
            return;
        }
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> this.finish());
    }
}
