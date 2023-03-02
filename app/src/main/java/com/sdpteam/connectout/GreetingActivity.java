package com.sdpteam.connectout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;

public class GreetingActivity extends AppCompatActivity {

    Authentication auth = new GoogleAuth();

    public void setAuthenticationService(Authentication auth) {
        this.auth = auth;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greeting);

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