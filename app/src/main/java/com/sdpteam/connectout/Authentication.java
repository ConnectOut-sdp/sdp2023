package com.sdpteam.connectout;

import android.content.Intent;

public interface Authentication {

    /**
     * @return true iff there is a currently logged user
     */
    boolean isLoggedIn();

    /**
     * @return the currently logged user, return null if not logged
     */
    AuthenticatedUser loggedUser();

    /**
     * log out the current user
     */
    void logout();

    /**
     * @return the intent to use to launch the UI that will signup/login the user
     */
    Intent buildIntent();
}

