package com.sdpteam.connectout.validation;

import static org.junit.Assert.*;
import org.junit.Test;

import android.content.Context;
import android.widget.EditText;

import androidx.test.platform.app.InstrumentationRegistry;

public class ValidationUtilsTest {

    @Test
    public void testHandleValidationFailure() {
        Context appContext = InstrumentationRegistry.getInstrumentation().getTargetContext();

        EditText editText = new EditText(appContext);
        String error = "Please enter a valid input";

        // Test for a successful validation
        boolean result1 = ValidationUtils.handleValidationFailure(true, editText, error);
        assertTrue(result1);
        assertNull(editText.getError());

        // Test for a failed validation
        boolean result2 = ValidationUtils.handleValidationFailure(false, editText, error);
        assertFalse(result2);
        assertEquals(error, editText.getError().toString());
    }
}
