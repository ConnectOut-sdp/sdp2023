package com.sdpteam.connectout.profileList;

import static com.sdpteam.connectout.event.viewer.EventActivity.PASSED_ID_KEY;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.utils.WithFragmentActivity;

/**
 * List view of the participants of an event
 */
public class EventParticipantsListActivity extends WithFragmentActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //Create the view of the activity.
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        Toolbar toolbar = findViewById(R.id.user_list_toolbar);
        Button filterButton = findViewById(R.id.user_list_button);
        setSupportActionBar(toolbar);
        toolbar.setNavigationOnClickListener(v -> this.finish());
        filterButton.setVisibility(View.GONE);

        String eventId = getIntent().getStringExtra(PASSED_ID_KEY);
        Bundle b = new Bundle();
        b.putString(PASSED_ID_KEY, eventId);
        Fragment f = new EventParticipantsListFragment();
        f.setArguments(b);
        replaceFragment(f, R.id.container_users_listview);
    }
}