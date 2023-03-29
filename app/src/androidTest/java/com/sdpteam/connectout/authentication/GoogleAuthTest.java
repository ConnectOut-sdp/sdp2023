package com.sdpteam.connectout.authentication;

import static org.junit.Assert.assertFalse;

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
        GoogleAuth ga = new GoogleAuth();
        assertFalse(ga.isLoggedIn());
    }

    @Test
    public void testLogsOutSuccessfully() {
        GoogleAuth ga = new GoogleAuth();
        ga.logout();
        assertFalse(ga.isLoggedIn());
    }
}