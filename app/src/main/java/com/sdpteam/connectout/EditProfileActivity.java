package com.sdpteam.connectout;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;

import com.google.gson.Gson;

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
                String jsonMyProfile;
                Profile myProfile = new Profile(-1, null, null, null,null);
                Bundle b = getIntent().getExtras();
                if(b != null){
                    jsonMyProfile = b.getString("myProfile");
                    myProfile = new Gson().fromJson(jsonMyProfile, Profile.class);

                }

                Profile newProfile = new Profile(myProfile.getId(), nameET.getText().toString(),
                        emailET.getText().toString(), bioET.getText().toString(), getGender(genderRG));

                //TODO store this new Profile

                goToProfile(newProfile);
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

    private void goToProfile(Profile p){
        Intent intent = new Intent(EditProfileActivity.this, TOBEREMOVEDProfileActivity.class);
        Bundle b = new Bundle();
        b.putString("myProfile", new Gson().toJson(p));
        //Json can be fetched back: new Gson().fromJson(jsonMyObject, MyObject.class);
        intent.putExtras(b);
        startActivity(intent);
        finish();
    }
}