package com.sdpteam.connectout.event;

import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.map.MapFragment;

public class EventCreatorActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_event_creator);

        //Retrieve tool bar and give it the control.
        Toolbar toolbar = findViewById(R.id.event_creator_toolbar);
        setSupportActionBar(toolbar);

        //set up the map fragment.
         Fragment mapFragment  = new MapFragment();
        replaceFragment(mapFragment, R.id.event_creator_fragment_container);

        //Upon click on the tool bar icon, end this activity and return to previous one.
        toolbar.setNavigationOnClickListener(v -> this.finish());

        EditText eventTitle = findViewById(R.id.event_creator_title);
        EditText eventDescription = findViewById(R.id.event_creator_description);
        Button saveButton = findViewById(R.id.event_creator_save_button);
        saveButton.setOnClickListener(v ->
        {
            Log.v("EditText", eventTitle.getText().toString());
            Log.v("EditText", eventDescription.getText().toString());
        });


    }

    /**
     * Replaces the current fragment within the container using the given one.
     *
     * @param fragment      (FragmentActivity): fragment to be used next
     * @param idOfContainer (int): id of the container where the fragment is stored
     */
    private void replaceFragment(Fragment fragment, int idOfContainer) {

        // Retrieve the fragment's handler
        FragmentManager fragmentManager = getSupportFragmentManager();

        // Initiate the swap of fragments
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the current fragment with the next one
        fragmentTransaction.replace(idOfContainer, fragment);

        fragmentTransaction.commit();
    }


}