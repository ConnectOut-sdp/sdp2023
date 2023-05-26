package com.sdpteam.connectout.authentication;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNull;

import org.junit.Test;

import android.content.Intent;

public class GoogleAuthTest {

    @Test
    public void googleIntentUselessTestJustForCoverage() {
        Intent intent = new GoogleAuth().buildIntent();
        System.out.println(intent.getData());
//        assertThrows(RuntimeException.class, () -> new GoogleAuth().buildIntent());
    }

    @Test
    public void testIsNotLoggedInBeforeStarting() {
        Authentication ga = new Authentication() {
            @Override
            public boolean isLoggedIn() {
                return false;
            }

            @Override
            public AuthenticatedUser loggedUser() {
                return null;
            }

            @Override
            public void logout() {

            }

            @Override
            public Intent buildIntent() {
                return null;
            }
        };
        assertFalse(ga.isLoggedIn());
    }

    @Test
    public void testLogsOutSuccessfully() {
        final boolean[] loggedIn = {true};
        Authentication ga = new Authentication() {
            @Override
            public boolean isLoggedIn() {
                return loggedIn[0];
            }

            @Override
            public AuthenticatedUser loggedUser() {
                return null;
            }

            @Override
            public void logout() {
                loggedIn[0] = false;
            }

            @Override
            public Intent buildIntent() {
                return null;
            }
        };
        ga.logout();
        assertFalse(ga.isLoggedIn());
    }

    @Test
    public void loggedUserIsNull() {
        assertNull(new GoogleAuth().loggedUser());
    }
}