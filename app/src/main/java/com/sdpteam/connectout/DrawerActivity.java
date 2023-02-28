package com.sdpteam.connectout;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.core.view.GravityCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.sdpteam.connectout.databinding.ActivityDrawerBinding;
import com.sdpteam.connectout.fragments.FilterFragment;
import com.sdpteam.connectout.fragments.HomeFragment;
import com.sdpteam.connectout.fragments.MyAccountFragment;
import com.sdpteam.connectout.fragments.MyEventsFragment;


public class DrawerActivity extends AppCompatActivity {
    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //Find drawer's principal view and initiate it.
        ActivityDrawerBinding binding = ActivityDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        //Retrieve tool bar and give it the control
        Toolbar toolbar  = binding.appBarDrawer.toolbar;
        setSupportActionBar(toolbar);

        //Retrieve drawer and navigation view instances
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;


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

            //Select next view change upon different options
            switch (item.getItemId()) {
                case R.id.menu_home:
                    //Go back to the Map/List view
                    replaceFragment(new HomeFragment(),R.id.drawer_fragment_container);
                    break;
                case R.id.menu_my_account:
                    //Go check out your account
                    replaceFragment(new MyAccountFragment(), R.id.drawer_fragment_container);
                    break;
                case R.id.menu_my_events:
                    //Go check out your events
                    replaceFragment(new MyEventsFragment(), R.id.drawer_fragment_container);
                    break;
                case R.id.menu_filters:
                    //Add add to the Map/List view, a TextEdit in order to filter what is displayed
                    FilterFragment filterFragment =  new FilterFragment();
                    replaceFragment(filterFragment,R.id.drawer_fragment_container);
                    break;
                case R.id.menu_logout:
                    Intent logOutIntent  = new Intent(getApplicationContext(), LogInActivity.class);
                    startActivity(logOutIntent);
                    break;
            }


            return true;
        });

    }

    /**
     *
     * @param fragment (Fragment): fragment to be used next
     * @param idOfContainer (int): id of the container where the fragment is stored
     */
    private void replaceFragment(Fragment fragment, int idOfContainer) {

        //Retrieve the fragment's handler
        FragmentManager fragmentManager = getSupportFragmentManager();

        //Initiate the swap of fragments
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        //Replace the current fragment with the next one
        fragmentTransaction.add(idOfContainer, fragment);
        fragmentTransaction.commit();
    }


}