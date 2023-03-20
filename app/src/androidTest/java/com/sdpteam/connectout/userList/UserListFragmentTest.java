package com.sdpteam.connectout.userList;
import static androidx.test.espresso.Espresso.onData;
import static androidx.test.espresso.Espresso.onView;
import static androidx.test.espresso.action.ViewActions.click;
import static androidx.test.espresso.matcher.ViewMatchers.withId;
import static androidx.test.platform.app.InstrumentationRegistry.getInstrumentation;
import static org.hamcrest.Matchers.anything;
import static org.junit.Assert.assertEquals;

import android.widget.ListView;

import androidx.fragment.app.testing.FragmentScenario;

import com.sdpteam.connectout.R;

import org.junit.Test;

import java.util.concurrent.atomic.AtomicInteger;

public class UserListFragmentTest {

    @Test
    public void userListDoesntCrashBeforeCreatingViewOnStopObservation(){
        UserListFragment ulf = new UserListFragment();
        ulf.stopObservation();
    }
    @Test
    public void userListDoesntCrashBeforeCreatingViewOnChangeObserver(){
        UserListFragment ulf = new UserListFragment();
        ulf.changeObserved(OrderingOption.NONE, null);
    }



}
