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
/*
        EditText t = findViewById(R.id.mainName);
        Button b = findViewById(R.id.mainGoButton);
        /*
        b.setOnClickListener(v -> {
            Intent myIntent = new Intent(getApplicationContext(), GreetingActivity.class);
            String msg = "Hello " + t.getText().toString() + "!";
            myIntent.putExtra("key", msg);
            MainActivity.this.startActivity(myIntent);
        });*/
    }
}