package com.sdpteam.connectout.validation;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import org.junit.Test;
import org.junit.runner.RunWith;

import android.content.Context;
import android.widget.EditText;
import android.widget.RadioButton;
import androidx.test.ext.junit.runners.AndroidJUnit4;
import androidx.test.platform.app.InstrumentationRegistry;

@RunWith(AndroidJUnit4.class)
public class EditProfileValidatorTest {

    @Test
    public void testIsValidEmail() {
        assertTrue(EditProfileValidator.isValidEmail("test@example.com"));
        assertFalse(EditProfileValidator.isValidEmail("example.com"));
    }

    @Test
    public void testIsValidName() {
        assertTrue(EditProfileValidator.isValidName("John"));
        assertFalse(EditProfileValidator.isValidName(""));
    }

    @Test
    public void testIsValidBio() {
        assertTrue(EditProfileValidator.isValidBio("This is a long bio.09809898089889"));
        assertFalse(EditProfileValidator.isValidBio("T"));
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
        assertTrue(EditProfileValidator.editProfileValidation(nameInput, emailInput, bioInput, male, female, other));

        // Test case 2: invalid email
        nameInput.setText("John");
        emailInput.setText("example.com");
        bioInput.setText("This is a long bio.");
        male.setChecked(true);
        assertFalse(EditProfileValidator.editProfileValidation(nameInput, emailInput, bioInput, male, female, other));
        assertNotNull(emailInput.getError());

        // Test case 3: invalid name
        nameInput.setText("");
        emailInput.setText("test@example.com");
        bioInput.setText("This is a long bio.");
        male.setChecked(true);
        assertFalse(EditProfileValidator.editProfileValidation(nameInput, emailInput, bioInput, male, female, other));
        assertNotNull(nameInput.getError());

        // Test case 4: invalid bio
        nameInput.setText("John");
        emailInput.setText("test@example.com");
        bioInput.setText("Too");
        male.setChecked(true);
        assertFalse(EditProfileValidator.editProfileValidation(nameInput, emailInput, bioInput, male, female, other));
        assertNotNull(bioInput.getError());
    }
}
