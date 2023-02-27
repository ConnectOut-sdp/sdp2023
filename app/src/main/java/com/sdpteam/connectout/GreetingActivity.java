package com.sdpteam.connectout;

import com.google.firebase.auth.FirebaseAuth;

import android.content.Intent;
import android.widget.Button;
import android.widget.TextView;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;

public class GreetingActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_greeting);

        Intent intent = getIntent();
        String value = intent.getStringExtra("loginInfo");

        TextView t = findViewById(R.id.greetingMessage);
        t.setText(value);

        Button logoutButton = findViewById(R.id.logoutButton);
        logoutButton.setOnClickListener(v -> logOut());
    }

    private void logOut() {
        FirebaseAuth.getInstance().signOut();
        finish(); // go to previous activity (login page)
    }
}