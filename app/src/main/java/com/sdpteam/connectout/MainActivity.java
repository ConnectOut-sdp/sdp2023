package com.sdpteam.connectout;

import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.firebase.ui.auth.data.model.FirebaseAuthUIAuthenticationResult;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private GoogleAuth googleAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        googleAuth = new GoogleAuth();

        Button b = findViewById(R.id.loginButton);
        b.setOnClickListener(v -> {
            signInLauncher.launch(googleAuth.buildIntent());
        });

        redirectIfAuthenticated();
    }

    final ActivityResultLauncher<Intent> signInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            new ActivityResultCallback<FirebaseAuthUIAuthenticationResult>() {
                @Override
                public void onActivityResult(FirebaseAuthUIAuthenticationResult result) {
                    System.out.println(result.getResultCode());
                    redirectIfAuthenticated();
                }
            });

    private void redirectIfAuthenticated() {
        if (googleAuth.isLoggedIn()) {
            AuthenticatedUser authenticatedUser = googleAuth.loggedUser();
            if (authenticatedUser != null) {
                navigateToSecondActivity(authenticatedUser);
            }
        }
    }

    private void navigateToSecondActivity(AuthenticatedUser currentUser) {
        Intent intent = new Intent(MainActivity.this, GreetingActivity.class);
        String msg = currentUser.name + " \n" + currentUser.email;
        intent.putExtra("loginInfo", msg);
        startActivity(intent);
    }
}