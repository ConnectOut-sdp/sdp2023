package com.sdpteam.connectout.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.sdpteam.connectout.R;
import com.sdpteam.connectout.registration.CompleteRegistrationActivity;

/**
 * Redirect automatically to next view (CompleteRegistrationActivity) if the user is already authenticated
 */
public class GoogleLoginActivity extends AppCompatActivity {
    private Authentication authentication = new GoogleAuth();
    private final ActivityResultLauncher<Intent> fireBaseSignInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            firebaseActivityResult -> {
                redirectIfAuthenticated();
            });

    void setAuthenticationService(Authentication a) {
        // useful for mocking in tests
        this.authentication = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_login);

        Button b = findViewById(R.id.loginButton);
        b.setOnClickListener(v -> {
            fireBaseSignInLauncher.launch(authentication.buildIntent());
        });

        redirectIfAuthenticated();
    }

    void redirectIfAuthenticated() {
        AuthenticatedUser authenticatedUser = authentication.loggedUser();
        if (authenticatedUser != null) {
            navigateToSecondActivity(authenticatedUser);
        }
    }

    private void navigateToSecondActivity(AuthenticatedUser currentUser) {
        Intent intent = new Intent(GoogleLoginActivity.this, CompleteRegistrationActivity.class);
        String msg = currentUser.name + " \n" + currentUser.email;
        intent.putExtra("loginInfo", msg);
        startActivity(intent);
    }
}