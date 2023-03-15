package com.sdpteam.connectout.drawer;

import com.google.android.material.navigation.NavigationView;
import com.sdpteam.connectout.R;
import com.sdpteam.connectout.WithFragmentActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

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

        // Upon button clicked of the drawer, replace current fragment with needed fragment
        navigationView.setNavigationItemSelectedListener(item -> {

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
            replaceFragment(new HomeFragment(), R.id.drawer_fragment_container);
        }
        if (itemId == R.id.menu_my_account) {
            //Go check out your account
            replaceFragment(new MyAccountFragment(), R.id.drawer_fragment_container);
        }
        if (itemId == R.id.menu_my_events) {
            //Go check out your events
            replaceFragment(new MyEventsFragment(), R.id.drawer_fragment_container);
        }
        if (itemId == R.id.menu_filters) {
            //Add add to the Map/List view, a TextEdit in order to filter what is displayed
            FilterFragment filterFragment = new FilterFragment();
            replaceFragment(filterFragment, R.id.drawer_fragment_container);
        }
        if (itemId == R.id.menu_logout) {
            Intent logOutIntent = new Intent(getApplicationContext(), LogInActivity.class);
            startActivity(logOutIntent);
        }
    }
}