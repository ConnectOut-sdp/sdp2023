package com.sdpteam.connectout;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Intent drawerIntent = new Intent(getApplicationContext(), DrawerActivity.class);
        this.startActivity(drawerIntent);

    }
}