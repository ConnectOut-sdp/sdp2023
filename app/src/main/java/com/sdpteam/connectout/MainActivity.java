package com.sdpteam.connectout;

import com.google.firebase.database.FirebaseDatabase;
import com.sdpteam.connectout.authentication.GoogleLoginActivity;
import com.sdpteam.connectout.event.creator.EventCreatorActivity;
import com.sdpteam.connectout.profile.EditProfileActivity;
import com.sdpteam.connectout.profile.ProfileActivity;
import com.sdpteam.connectout.qr_code.QRcodeActivity;
import com.sdpteam.connectout.qr_code.QRcodeProfileActivity;
import com.sdpteam.connectout.registration.CompleteRegistrationActivity;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // enabling persistence for offline queries
        FirebaseDatabase.getInstance().setPersistenceEnabled(true);

        // Don't put anything here, just choose which activity to redirect to
        Intent drawerIntent = new Intent(getApplicationContext(), ProfileActivity.class);
        this.startActivity(drawerIntent);
    }
}