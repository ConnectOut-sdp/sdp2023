package com.sdpteam.connectout.profile;

import com.sdpteam.connectout.R;
import com.sdpteam.connectout.authentication.Authentication;
import com.sdpteam.connectout.authentication.GoogleAuth;
import com.sdpteam.connectout.validation.EditProfileValidator;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import androidx.appcompat.app.AppCompatActivity;

public class EditProfileActivity extends AppCompatActivity {
    public final static String NULL_USER = "null_user";
    ProfileViewModel profileViewModel = new ProfileViewModel(new ProfileFirebaseDataSource());
    Authentication auth = new GoogleAuth();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        Button save = findViewById(R.id.saveButton);
        EditText nameET = findViewById(R.id.editTextName);
        EditText emailET = findViewById(R.id.editTextEmail);
        EditText bioET = findViewById(R.id.editTextBio);
        RadioGroup genderRG = findViewById(R.id.genderRadioGroup);
        RadioButton male = findViewById(R.id.maleRadioButton);
        RadioButton female = findViewById(R.id.femaleRadioButton);
        RadioButton other = findViewById(R.id.otherRadioButton);

        other.setChecked(true);

        save.setOnClickListener(v -> {
            String uid = auth.isLoggedIn() ? auth.loggedUser().uid : NULL_USER;

            // validation
            if(EditProfileValidator.editProfileValidation(nameET, emailET, bioET, male, female, other)) {
                Profile newProfile = new Profile(uid, nameET.getText().toString(), emailET.getText().toString(), bioET.getText().toString(), getGender(male, female, other), 1, 1, "");
                profileViewModel.saveProfile(newProfile);
                goToProfile(newProfile);
            }
        });
    }

    private Profile.Gender getGender(RadioButton male, RadioButton female, RadioButton other) {
        if (male.isChecked()) {
            return Profile.Gender.MALE;
        } else if (female.isChecked()) {
            return Profile.Gender.FEMALE;
        } else if (other.isChecked()) {
            return Profile.Gender.OTHER;
        }
        return null;
    }

    private void goToProfile(Profile p) {
        Intent intent = new Intent(EditProfileActivity.this, ProfileActivity.class);
        startActivity(intent);
        finish();
    }
}