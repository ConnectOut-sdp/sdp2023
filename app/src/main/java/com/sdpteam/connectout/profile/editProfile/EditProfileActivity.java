package com.sdpteam.connectout.profile.editProfile;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.profile.Profile;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {

    public static Profile profileToEdit = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.container, EditProfileForm.newInstance(profileToEdit))
                    .commitNow();
        }

        Button closeBtn = findViewById(R.id.cancelButton);
        closeBtn.setOnClickListener(v -> this.finish());
        closeBtn.setVisibility(profileToEdit == null ? View.GONE : View.VISIBLE);
    }
}