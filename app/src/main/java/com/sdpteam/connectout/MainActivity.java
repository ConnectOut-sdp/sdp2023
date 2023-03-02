package com.sdpteam.connectout;

import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private Authentication authentication = new GoogleAuth();

    public void setAuthenticationService(Authentication a) {
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
        Intent intent = new Intent(MainActivity.this, GreetingActivity.class);
        String msg = currentUser.name + " \n" + currentUser.email;
        intent.putExtra("loginInfo", msg);
        startActivity(intent);
    }
}