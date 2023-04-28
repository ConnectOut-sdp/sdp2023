package com.sdpteam.connectout.authentication;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.sdpteam.connectout.R;

public class ProfileGreetingActivity extends AppCompatActivity {

    private Authentication auth = new GoogleAuth();

    void setAuthenticationService(Authentication auth) {
        // useful for mocking in tests
        this.auth = auth;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_greeting);

        Intent intent = getIntent();
        String value = intent.getStringExtra("loginInfo");
        // not necessary could use googleAuth and ask him directly informations

        TextView t = findViewById(R.id.greetingMessage);
        t.setText(value);

        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> logOut());
    }

    private void logOut() {
        auth.logout();
        finish(); // go to previous activity (login page)
    }
}