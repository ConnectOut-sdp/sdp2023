package com.sdpteam.connectout.authentication;

import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.sdpteam.connectout.R;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

public class GoogleLoginActivity extends AppCompatActivity {
    private Authentication authentication = new GoogleAuth();

    void setAuthenticationService(Authentication a) {
        // useful for mocking in tests
        this.authentication = a;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button b = findViewById(R.id.loginButton);
        b.setOnClickListener(v -> {
            fireBaseSignInLauncher.launch(authentication.buildIntent());
        });

        redirectIfAuthenticated();
    }

    final ActivityResultLauncher<Intent> fireBaseSignInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            firebaseActivityResult -> {
                redirectIfAuthenticated();
            });

    void redirectIfAuthenticated() {
        AuthenticatedUser authenticatedUser = authentication.loggedUser();
        if (authenticatedUser != null) {
            navigateToSecondActivity(authenticatedUser);
        }
    }

    private void navigateToSecondActivity(AuthenticatedUser currentUser) {
        Intent intent = new Intent(GoogleLoginActivity.this, ProfileGreetingActivity.class);
        String msg = currentUser.name + " \n" + currentUser.email;
        intent.putExtra("loginInfo", msg);
        startActivity(intent);
    }
}