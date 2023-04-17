package com.sdpteam.connectout.validation;

import static com.sdpteam.connectout.validation.ValidationUtils.handleValidationFailure;

import android.util.Patterns;
import android.widget.EditText;
import android.widget.RadioButton;

public class EditProfileValidator {

    private static final int MIN_PASSWORD_LENGTH = 6;
    private static final int MIN_NAME_LENGTH = 1;
    private static final int MIN_BIO_LENGTH = 10;

    public static final String EMAIL_ERROR = "Invalid email address";
    public static final String NAME_ERROR = "Name should be longer";
    public static final String PASSWORD_ERROR = "Password not strong enough. Use at least " + MIN_PASSWORD_LENGTH + " characters and a mix of letters and numbers";
    public static final String BIO_ERROR = "Bio not long enough. Should be at least " + MIN_BIO_LENGTH + " characters long";

    public static final String GENDER_ERROR = "One gender must be selected !";

    public static boolean isValidEmail(CharSequence email) {
        return Patterns.EMAIL_ADDRESS.matcher(email).matches();
    }

    public static boolean isValidPassword(String password) {
        return password.length() >= MIN_PASSWORD_LENGTH;
    }

    public static boolean isValidName(String name) {
        return name.length() >= MIN_NAME_LENGTH;
    }

    public static boolean isValidBio(String bio) {
        return bio.length() >= MIN_BIO_LENGTH;
    }

    public static boolean editProfileValidation(EditText nameInput, EditText emailInput, EditText bioInput, RadioButton male, RadioButton female, RadioButton other) {

        if(!handleValidationFailure(isValidName(nameInput.getText().toString()), nameInput, NAME_ERROR)
                || !handleValidationFailure(isValidEmail(emailInput.getText().toString()), emailInput, EMAIL_ERROR)
                || !handleValidationFailure(isValidBio(bioInput.getText().toString()), bioInput, BIO_ERROR)) {
            return false;
        }
        if(!male.isChecked() && !female.isChecked() && !other.isChecked()) {
            female.setError(GENDER_ERROR);
            nameInput.requestFocus();
            return false;
        }
        return true;
    }


}

