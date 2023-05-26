package com.sdpteam.connectout.profileList;

import static com.sdpteam.connectout.event.viewer.EventActivity.PASSED_ID_KEY;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.profileList.filter.ProfileParticipationFilter;
import com.sdpteam.connectout.utils.WithFragmentActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

/**
 * List view of the participants of an event
 */
public class EventParticipantsListActivity extends WithFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Create the view of the activity.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_community);

        final Toolbar toolbar = findViewById(R.id.user_list_toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> this.finish());

        final Button filterButton = findViewById(R.id.user_list_button);
        filterButton.setVisibility(View.GONE);

        final String eventId = getIntent().getStringExtra(PASSED_ID_KEY);

        final Fragment f = new FilteredProfileListFragment(new ProfileParticipationFilter(eventId));
        replaceFragment(f, R.id.container_users_listview);
    }

    public static void showParticipants(Context context, String eventId) {
        final Intent intent = new Intent(context, EventParticipantsListActivity.class);
        intent.putExtra(PASSED_ID_KEY, eventId);
        context.startActivity(intent);
    }
}