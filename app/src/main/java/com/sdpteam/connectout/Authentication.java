package com.sdpteam.connectout;

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
}

