package com.sdpteam.connectout;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.View;
import android.view.Menu;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.navigation.NavigationView;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import androidx.navigation.ui.AppBarConfiguration;
import androidx.navigation.ui.NavigationUI;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.appcompat.app.AppCompatActivity;

import com.sdpteam.connectout.databinding.ActivityDrawerBinding;
import com.sdpteam.connectout.ui.filter.FilterFragment;
import com.sdpteam.connectout.ui.home.HomeFragment;
import com.sdpteam.connectout.ui.myAccount.MyAccountFragment;
import com.sdpteam.connectout.ui.myEvents.MyEventsFragment;

public class DrawerActivity extends AppCompatActivity {

    @SuppressLint("NonConstantResourceId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        com.sdpteam.connectout.databinding.ActivityDrawerBinding binding = ActivityDrawerBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        Toolbar toolbar  = binding.appBarDrawer.toolbar;
        setSupportActionBar(toolbar);
        DrawerLayout drawer = binding.drawerLayout;
        NavigationView navigationView = binding.navView;


        toolbar.setNavigationOnClickListener(v -> drawer.open());
        ActionBarDrawerToggle drawerToggle = new ActionBarDrawerToggle(this, drawer, R.string.open_drawer, R.string.close_drawer);
        drawer.addDrawerListener(drawerToggle);
        drawerToggle.syncState(); //Is okay since in the main

        navigationView.setNavigationItemSelectedListener(item -> {

            item.setChecked(true);
            drawer.close();

            switch (item.getItemId()) {
                case R.id.nav_home:
                    replaceFragment(new HomeFragment(),R.id.drawer_fragment_container);
                    break;
                case R.id.account:
                    replaceFragment(new MyAccountFragment(), R.id.drawer_fragment_container);
                    break;
                case R.id.myEvents:
                    replaceFragment(new MyEventsFragment(), R.id.drawer_fragment_container);
                    break;
                case R.id.filters:
                    replaceFragment(new FilterFragment(), R.id.drawer_fragment_container);
                    break;
            }

            return true;
        });

    }

    private void replaceFragment(Fragment fragment, int idOfContainer) {

        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(idOfContainer, fragment);
        fragmentTransaction.commit();
    }


}