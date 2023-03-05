package com.sdpteam.connectout.profile;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.google.gson.Gson;
import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.AuthenticatedUser;
import com.sdpteam.connectout.authentication.GoogleAuth;

public class EditProfileActivity extends AppCompatActivity {
    public final static String NULL_USER = "null_user";
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        Button save = findViewById(R.id.saveButton);
        EditText nameET = findViewById(R.id.editTextName);
        EditText emailET = findViewById(R.id.editTextEmail);
        EditText bioET = findViewById(R.id.editTextBio);
        RadioGroup genderRG = findViewById(R.id.genderRadioGroup);

        nameET.setOnEditorActionListener(new EditText.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE) {
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(nameET.getWindowToken(), 0);
                    return true;
                }
                return false;
            }
        });

        save.setOnClickListener(v -> {
            AuthenticatedUser au = new GoogleAuth().loggedUser();
            //get new Values
            Profile newProfile = new Profile((au == null)? NULL_USER: au.uid, nameET.getText().toString(),
                    emailET.getText().toString(), bioET.getText().toString(), getGender(genderRG), 1, 1);

            //store new values
            new ProfileViewModel(new ProfileModel()).saveValue(newProfile);

            //change view
            goToProfile(newProfile);
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
        startActivity(intent);
        finish();
    }
}