package com.sdpteam.connectout.userList;

import org.junit.Test;

public class UserListFragmentTest {

    @Test
    public void userListDoesntCrashBeforeCreatingViewOnStopObservation() {
        UserListFragment ulf = new UserListFragment();
        ulf.stopObservation();
    }

    @Test
    public void userListDoesntCrashBeforeCreatingViewOnChangeObserver() {
        UserListFragment ulf = new UserListFragment();
        ulf.changeObserved(OrderingOption.NONE, null);
    }


}
