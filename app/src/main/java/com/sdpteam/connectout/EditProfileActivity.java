package com.sdpteam.connectout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

public class EditProfileActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Button save = findViewById(R.id.saveButton);
        EditText nameET = findViewById(R.id.editTextName);
        EditText emailET = findViewById(R.id.editTextEmail);
        EditText bioET = findViewById(R.id.editTextBio);
        RadioGroup genderRG = findViewById(R.id.genderRadioGroup);

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int profileID = -1;
                Bundle b = getIntent().getExtras();
                if (b != null) {
                    profileID = Integer.getInteger(b.getString("key"));
                }

                Profile newProfile = new Profile(profileID,nameET.getText().toString(),
                        emailET.getText().toString(), bioET.getText().toString(), getGender(genderRG));

                //store this new Profile

                goToProfile(profileID);
            }
        });
    }

    private Profile.Gender getGender(RadioGroup genderRG){
        switch (genderRG.getCheckedRadioButtonId()){
            case R.id.femaleRadioButton:
                return Profile.Gender.FEMALE;
            case R.id.maleRadioButton:
                return Profile.Gender.MALE;
            case R.id.otherRadioButton:
                return Profile.Gender.OTHER;
            default:
                return null;
        }
    }

    private void goToProfile(int profileID){
        Intent intent = new Intent(EditProfileActivity.this, TOBEREMOVEDProfileActivity.class);
        Bundle b = new Bundle();
        b.putString("profileID", Integer.toString(profileID));
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }
}