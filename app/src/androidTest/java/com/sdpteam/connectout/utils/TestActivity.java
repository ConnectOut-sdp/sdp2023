package com.sdpteam.connectout.utils;

import com.sdpteam.connectout.R;

import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

/**
 * To test fragments
 * Must be here otherwise cannot launch it from the test folders, because must be in AndroidManifest.xml
 */
public class TestActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
    }
}
