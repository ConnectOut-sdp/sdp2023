package com.sdpteam.connectout.registration;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;

import com.sdpteam.connectout.R;

public class CompleteRegistrationActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complete_registration);
        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, CompleteRegistrationForm.newInstance())
                    .commitNow();
        }
    }
}