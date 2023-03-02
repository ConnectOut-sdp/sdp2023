package com.sdpteam.connectout;

import java.util.Arrays;
import java.util.List;

import com.firebase.ui.auth.AuthUI;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

import android.content.Intent;

// https://firebase.google.com/docs/auth/android/firebaseui
public class GoogleAuth implements Authentication {

    @Override
    public boolean isLoggedIn() {
        return FirebaseAuth.getInstance().getCurrentUser() != null;
    }

    @Override
    public AuthenticatedUser loggedUser() {
        return firebaseUserToAuthenticatedUser(FirebaseAuth.getInstance().getCurrentUser());
    }

    @Override
    public void logout() {
        FirebaseAuth.getInstance().signOut();
    }

    /**
     * Parameters for Google's login UI
     */
    @Override
    public Intent buildIntent() {
        // Choose authentication providers
        List<AuthUI.IdpConfig> providers = Arrays.asList(
                new AuthUI.IdpConfig.EmailBuilder().build(),
                new AuthUI.IdpConfig.GoogleBuilder().build());

        return AuthUI.getInstance()
                .createSignInIntentBuilder()
                .setAvailableProviders(providers)
                .setAlwaysShowSignInMethodScreen(true)
                .setIsSmartLockEnabled(false)
                .build();
    }

    private AuthenticatedUser firebaseUserToAuthenticatedUser(FirebaseUser currentUser) {
        if (currentUser == null) {
            return null;
        }
        return new AuthenticatedUser(currentUser.getUid(), currentUser.getDisplayName(), currentUser.getEmail());
    }
}
