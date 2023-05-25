package com.sdpteam.connectout.authentication;

import com.firebase.ui.auth.FirebaseAuthUIActivityResultContract;
import com.sdpteam.connectout.R;
import com.sdpteam.connectout.drawer.DrawerActivity;
import com.sdpteam.connectout.profile.ProfileFirebaseDataSource;
import com.sdpteam.connectout.profile.editProfile.EditProfileActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;
import androidx.activity.result.ActivityResultLauncher;
import androidx.appcompat.app.AppCompatActivity;

/**
 * Redirect automatically to next view (CompleteRegistrationActivity) if the user is already authenticated
 */
public class GoogleLoginActivity extends AppCompatActivity {
    public static Authentication authentication = new GoogleAuth();
    private Button loginBtn;
    private ProgressBar progressBar;
    private TextView infoText;
    private final ActivityResultLauncher<Intent> fireBaseSignInLauncher = registerForActivityResult(
            new FirebaseAuthUIActivityResultContract(),
            firebaseActivityResult -> {
                redirectIfAuthenticated();
            });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_google_login);

        loginBtn = findViewById(R.id.loginButton);
        loginBtn.setOnClickListener(v -> {
            fireBaseSignInLauncher.launch(authentication.buildIntent());
        });

        progressBar = findViewById(R.id.progressBar);
        progressBar.setVisibility(View.GONE);

        infoText = findViewById(R.id.infoText);
        infoText.setVisibility(View.GONE);

        redirectIfAuthenticated();
    }

    @SuppressLint("SetTextI18n")
    void redirectIfAuthenticated() {
        AuthenticatedUser authenticatedUser = authentication.loggedUser();
        if (authenticatedUser != null) {
            loginBtn.setVisibility(View.GONE);

            infoText.setVisibility(View.VISIBLE);
            infoText.setText(getString(R.string.hello) + authenticatedUser.name + getString(R.string.you_will_be_redirected_in_few_seconds));

            progressBar.setVisibility(View.VISIBLE);

            navigateToSecondActivity(authenticatedUser);
        }
    }

    private void navigateToSecondActivity(AuthenticatedUser currentUser) {
        new ProfileFirebaseDataSource().fetchProfile(currentUser.uid).thenAccept(profile -> {
            finish();
            Intent intent = new Intent(GoogleLoginActivity.this, profile == null ? EditProfileActivity.class : DrawerActivity.class);
            intent.putExtra("loginInfo", currentUser.name + " \n" + currentUser.email);
            EditProfileActivity.profileToEdit = profile; // profile to complete, because intent does not work with Profile object
            startActivity(intent);
        });
    }
}