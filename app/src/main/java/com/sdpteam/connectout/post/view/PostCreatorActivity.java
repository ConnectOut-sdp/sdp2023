package com.sdpteam.connectout.post.view;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.profile.ProfileActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;

public class PostCreatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_creator);

        final String profileId = getIntent().getStringExtra("profileId");
        final String eventId = getIntent().getStringExtra("eventId");
        final String eventName = getIntent().getStringExtra("eventName");

        initToolbar();
        initTitle(eventName);
    }

    private void initTitle(String eventName) {
        TextView t = findViewById(R.id.post_creator_title_description);
        t.setText("Creating a post for event \"" + eventName + "\"");
    }

    private void initToolbar() {
        Toolbar toolbar = findViewById(R.id.event_creator_toolbar);
        if (toolbar == null) return;
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> this.finish());
    }

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
}
