package com.sdpteam.connectout;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.sdpteam.connectout.data.BoredActivityRepository;

public class BoredApiButtonActivity extends AppCompatActivity {

    private BoredActivityRepository repository;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bored_api_button);


    }
}