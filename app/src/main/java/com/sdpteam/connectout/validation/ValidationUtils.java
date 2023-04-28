package com.sdpteam.connectout.validation;

import android.widget.EditText;

public class ValidationUtils {
    public static boolean handleValidationFailure(boolean condition, EditText input, String error) {
        if (!condition) {
            input.setError(error);
            input.requestFocus();
        }
        return condition;
    }
}
