package com.sdpteam.connectout.drawer;

import com.google.android.material.navigation.NavigationView;
import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.GoogleAuth;
import com.sdpteam.connectout.authentication.GoogleLoginActivity;
import com.sdpteam.connectout.event.creator.EventCreatorActivity;
import com.sdpteam.connectout.event.nearbyEvents.EventsFragment;
import com.sdpteam.connectout.event.viewer.MyEventsCalendarFragment;
import com.sdpteam.connectout.profile.ProfileFragment;
import com.sdpteam.connectout.profileList.ProfilesContainerFragment;
import com.sdpteam.connectout.utils.WithFragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;

public class DrawerActivity extends WithFragmentActivity {
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Find drawer's principal view and initiate it.
        setContentView(R.layout.activity_drawer);

        //Retrieve tool bar and give it the control
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //Retrieve drawer and navigation view instances
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);

        //Upon click on the tool bar icon, open the drawer of options
        toolbar.setNavigationOnClickListener(v -> drawer.openDrawer(GravityCompat.START));

        //Instantiate a drawer toggle, so upon listening to changes, the drawer closes or opens up.
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.open_drawer, R.string.close_drawer);
        drawer.addDrawerListener(drawerToggle);
        replaceFragment(new EventsFragment(), R.id.drawer_fragment_container);

        // Upon button clicked of the drawer, replace current fragment with needed fragment
        navigationView.setNavigationItemSelectedListener(item -> {
            findViewById(R.id.drawer_button).setVisibility(View.GONE);
            //Show the item that is checked upon re-opening the drawer
            item.setChecked(true);
            //closes the drawer upon receiving a click on an option
            drawer.close();
            //set up new fragment
            displayFragment(item.getItemId());

            return true;
        });
    }

    /**
     * Displays the selected fragment according to its id.
     *
     * @param itemId (int): Id of the wanted fragment
     */
    private void displayFragment(int itemId) {
        //Select next view change upon different options
        if (itemId == R.id.menu_home) {
            //Go back to the Map/List view
            replaceFragment(new EventsFragment(), R.id.drawer_fragment_container);
        }
        if (itemId == R.id.menu_my_account) {
            //Go check out your account
            replaceFragment(new ProfileFragment(), R.id.drawer_fragment_container);
        }
        if (itemId == R.id.menu_my_events) {
            //Go check out your events
            replaceFragment(new MyEventsCalendarFragment(), R.id.drawer_fragment_container);
        }
        if (itemId == R.id.menu_profiles) {
            //Go check out other peoples
            replaceFragment(new ProfilesContainerFragment(), R.id.drawer_fragment_container);
        }
        if (itemId == R.id.menu_add) {
            Intent creationIntent = new Intent(getApplicationContext(), EventCreatorActivity.class);
            startActivity(creationIntent);
            replaceFragment(new EventsFragment(), R.id.drawer_fragment_container);
        }
        if (itemId == R.id.menu_logout) {
            new GoogleAuth().logout();
            Intent logOutIntent = new Intent(getApplicationContext(), GoogleLoginActivity.class);
            startActivity(logOutIntent);
        }
    }

    public void setupButton(String text, View.OnClickListener listener) {
        Button button = findViewById(R.id.drawer_button);
        button.setOnClickListener(listener);
        button.setText(text);
        button.setVisibility(View.VISIBLE);
    }
}