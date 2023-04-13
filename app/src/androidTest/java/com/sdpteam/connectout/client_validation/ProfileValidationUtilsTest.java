package com.sdpteam.connectout.client_validation;

import android.content.Context;
import android.widget.EditText;
import android.widget.RadioButton;

import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

@RunWith(AndroidJUnit4.class)
public class ProfileValidationUtilsTest {

    @Test
    public void testIsValidEmail() {
        assertTrue(ProfileValidationUtils.isValidEmail("test@example.com"));
        assertFalse(ProfileValidationUtils.isValidEmail("example.com"));
    }

    @Test
    public void testIsValidPassword() {
        assertTrue(ProfileValidationUtils.isValidPassword("password12376876"));
        assertFalse(ProfileValidationUtils.isValidPassword("123"));
    }

    @Test
    public void testIsValidName() {
        assertTrue(ProfileValidationUtils.isValidName("John"));
        assertFalse(ProfileValidationUtils.isValidName(""));
    }

    @Test
    public void testIsValidBio() {
        assertTrue(ProfileValidationUtils.isValidBio("This is a long bio.09809898089889"));
        assertFalse(ProfileValidationUtils.isValidBio("T"));
    }

    @Test
    public void testEditProfileValidation() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        EditText nameInput = new EditText(appContext);
        EditText emailInput = new EditText(appContext);
        EditText bioInput = new EditText(appContext);
        RadioButton male = new RadioButton(appContext);
        RadioButton female = new RadioButton(appContext);
        RadioButton other = new RadioButton(appContext);

        // Test case 1: valid input
        nameInput.setText("John");
        emailInput.setText("test@example.com");
        bioInput.setText("This is a long bio.");
        male.setChecked(true);
        assertTrue(ProfileValidationUtils.editProfileValidation(nameInput, emailInput, bioInput, male, female, other));

        // Test case 2: invalid email
        nameInput.setText("John");
        emailInput.setText("example.com");
        bioInput.setText("This is a long bio.");
        male.setChecked(true);
        assertFalse(ProfileValidationUtils.editProfileValidation(nameInput, emailInput, bioInput, male, female, other));
        assertNotNull(emailInput.getError());

        // Test case 3: invalid name
        nameInput.setText("");
        emailInput.setText("test@example.com");
        bioInput.setText("This is a long bio.");
        male.setChecked(true);
        assertFalse(ProfileValidationUtils.editProfileValidation(nameInput, emailInput, bioInput, male, female, other));
        assertNotNull(nameInput.getError());

        // Test case 4: invalid bio
        nameInput.setText("John");
        emailInput.setText("test@example.com");
        bioInput.setText("Too");
        male.setChecked(true);
        assertFalse(ProfileValidationUtils.editProfileValidation(nameInput, emailInput, bioInput, male, female, other));
        assertNotNull(bioInput.getError());

        // Test case 5: no gender selected
        nameInput.setText("John");
        emailInput.setText("test@example.com");
        bioInput.setText("This is a long bio.986780897987");
        male.setChecked(false);
        female.setChecked(false);
        other.setChecked(false);
        assertFalse(ProfileValidationUtils.editProfileValidation(nameInput, emailInput, bioInput, male, female, other));
        assertNotNull(female.getError());
    }

}
