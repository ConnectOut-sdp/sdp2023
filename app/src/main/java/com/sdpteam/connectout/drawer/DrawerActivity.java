package com.sdpteam.connectout.drawer;

import com.google.android.material.navigation.NavigationView;
import com.sdpteam.connectout.QrCode.QRcodeActivity;
import com.sdpteam.connectout.QrCode.QRcodeModalActivity;
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
import androidx.fragment.app.Fragment;

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
        Fragment fragment = null;
        int containerId = R.id.drawer_fragment_container;

        switch (itemId) {
            case R.id.menu_home:
                fragment = new EventsFragment();
                break;
            case R.id.menu_my_account:
                fragment = new ProfileFragment();
                break;
            case R.id.menu_my_events:
                fragment = new MyEventsCalendarFragment();
                break;
            case R.id.menu_community:
                fragment = new ProfilesContainerFragment();
                break;
            case R.id.scan_qr_code:
                startActivity(new Intent(DrawerActivity.this, QRcodeActivity.class));
                return;
            case R.id.menu_logout:
                new GoogleAuth().logout();
                startActivity(new Intent(getApplicationContext(), GoogleLoginActivity.class));
                return;
        }

        if (fragment != null) {
            replaceFragment(fragment, containerId);
        }
    }



    public void setupButton(String text, View.OnClickListener listener) {
        Button button = findViewById(R.id.drawer_button);
        button.setOnClickListener(listener);
        button.setText(text);
        button.setVisibility(View.VISIBLE);
    }
}