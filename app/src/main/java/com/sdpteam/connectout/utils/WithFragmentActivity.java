package com.sdpteam.connectout.utils;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

public class WithFragmentActivity extends AppCompatActivity {
    /**
     * Replaces the current fragment within the container using the given one.
     *
     * @param fragment    (Fragment): fragment to be used next
     * @param containerId (int): id of the container where the fragment is stored
     */
    public void replaceFragment(Fragment fragment, int containerId) {

        // Retrieve the fragment's handler
        FragmentManager fragmentManager = this.getSupportFragmentManager();

        // Initiate the swap of fragments
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();

        // Replace the current fragment with the next one
        fragmentTransaction.replace(containerId, fragment);

        fragmentTransaction.commit();
    }
}
