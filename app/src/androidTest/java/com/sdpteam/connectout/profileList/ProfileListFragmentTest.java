package com.sdpteam.connectout.profileList;

import org.junit.Test;

public class ProfileListFragmentTest {

    @Test
    public void userListDoesntCrashBeforeCreatingViewOnStopObservation() {
        ProfileListFragment ulf = new ProfileListFragment();
        ulf.stopObservation();
    }

    @Test
    public void userListDoesntCrashBeforeCreatingViewOnChangeObserver() {
        ProfileListFragment ulf = new ProfileListFragment();
        ulf.changeObserved(OrderingOption.NONE, null);
    }


}
