package com.sdpteam.connectout;

import static org.junit.Assert.assertEquals;

import org.junit.Test;

public class AuthenticatedUserTest {

    @Test
    public void testUselessStuffJustForCoverage() {
        AuthenticatedUser authenticatedUser = new AuthenticatedUser("007", "I am", "Your father");
        assertEquals("007", authenticatedUser.uid);
    }
}